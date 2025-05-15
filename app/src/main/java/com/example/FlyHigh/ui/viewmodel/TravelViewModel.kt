package com.example.FlyHigh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.dao.UserDao
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.data.local.entity.TripEntity
import com.example.FlyHigh.data.local.entity.UserEntity
import com.example.FlyHigh.domain.model.TripImage
import com.example.FlyHigh.domain.repository.TripImageInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TravelViewModel @Inject constructor(
    private val tripDao: TripDao,
    private val itineraryItemDao: ItineraryItemDao,
    private val userDao: UserDao,
    private val tripImageRepo: TripImageInterface
) : ViewModel() {

    private val TAG = "TravelViewModel"

    private val _currentUser = MutableLiveData<UserEntity?>()
    val currentUser: LiveData<UserEntity?> = _currentUser

    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    private val _tripsRefreshTrigger = MutableStateFlow(0)
    val tripsRefreshTrigger: StateFlow<Int> = _tripsRefreshTrigger.asStateFlow()

    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        viewModelScope.launch {
            if (auth.currentUser != null) {
                loadCurrentUser()
            } else {
                _currentUser.value = null
                _currentUserId.value = null
                refreshTrips()
            }
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
        loadCurrentUser()
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                try {
                    var user = withContext(Dispatchers.IO) {
                        userDao.getUserByFirebaseUid(firebaseUser.uid)
                    }
                    if (user == null) {
                        val userId = createLocalUserFromFirebase(firebaseUser)
                        user = withContext(Dispatchers.IO) {
                            userDao.getUserByIdSuspend(userId)
                        }
                    }
                    _currentUser.value = user
                    _currentUserId.value = user?.id
                    refreshTrips()
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading user: ${e.message}", e)
                }
            } else {
                _currentUser.value = null
                _currentUserId.value = null
                refreshTrips()
            }
        }
    }

    private suspend fun createLocalUserFromFirebase(firebaseUser: FirebaseUser): Long {
        return withContext(Dispatchers.IO) {
            try {
                val newUser = UserEntity(
                    firebaseUid = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "user_${firebaseUser.uid.take(5)}",
                    email = firebaseUser.email ?: "",
                    birthDate = Date(),
                    address = "",
                    country = "",
                    phoneNumber = firebaseUser.phoneNumber ?: "",
                    acceptEmailsOffers = false
                )
                userDao.insertUser(newUser)
            } catch (e: Exception) {
                Log.e(TAG, "Error creating local user: ${e.message}", e)
                throw e
            }
        }
    }

    fun refreshTrips() {
        _tripsRefreshTrigger.value = _tripsRefreshTrigger.value + 1
    }

    fun getTripById(tripId: Long): Flow<TripEntity?> {
        val userId = _currentUserId.value
        return if (userId != null) {
            tripDao.getTripByIdAndUserId(tripId, userId)
        } else {
            MutableStateFlow(null)
        }
    }

    fun getAllTrips(): Flow<List<TripEntity>> {
        return combine(_currentUserId, _tripsRefreshTrigger.asStateFlow()) { userId, _ ->
            if (userId != null) {
                withContext(Dispatchers.IO) {
                    tripDao.getTripsByUserId(userId).asFlow().first()
                }
            } else {
                emptyList()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun addTravel(title: String, destination: String, startDate: Date, endDate: Date, description: String, imageUrl: String?) {
        viewModelScope.launch {
            try {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser == null) return@launch

                var userId = _currentUserId.value
                if (userId == null) {
                    loadCurrentUser()
                    delay(500)
                    userId = _currentUserId.value
                    if (userId == null) {
                        val localUser = getUserByFirebaseUid(firebaseUser.uid)
                        userId = localUser?.id ?: createLocalUserFromFirebase(firebaseUser)
                    }
                }

                if (userId == null || userId <= 0) return@launch

                val trip = TripEntity(
                    userId = userId,
                    title = title,
                    destination = destination,
                    startDate = startDate,
                    endDate = endDate,
                    description = description,
                    imageUrl = imageUrl
                )
                tripDao.insertTrip(trip)
                refreshTrips()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding travel: ${e.message}", e)
            }
        }
    }

    fun updateTravel(updatedTrip: TripEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUserId = _currentUserId.value
            if (currentUserId != null && updatedTrip.userId == currentUserId) {
                tripDao.updateTrip(updatedTrip)
                refreshTrips()
            }
        }
    }

    fun deleteTravel(tripId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = _currentUserId.value
            if (userId != null) {
                try {
                    tripDao.deleteTripById(tripId, userId)
                    refreshTrips()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to delete trip: ${e.message}")
                }
            }
        }
    }

    fun getItineraryById(itineraryId: Long): Flow<ItineraryItemEntity?> {
        return itineraryItemDao.getItineraryItemById(itineraryId)
    }

    fun addItinerary(tripId: Long, title: String, description: String, location: String, date: Date, startTime: Date?, endTime: Date?, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = _currentUserId.value ?: return@launch
            tripDao.getTripByIdAndUserId(tripId, userId).collect { tripEntity ->
                if (tripEntity != null) {
                    val itinerary = ItineraryItemEntity(
                        tripId = tripId,
                        title = title,
                        description = description,
                        location = location,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        type = type
                    )
                    itineraryItemDao.insertItineraryItem(itinerary)
                }
            }
        }
    }

    fun updateItinerary(updatedItinerary: ItineraryItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = _currentUserId.value ?: return@launch
            tripDao.getTripByIdAndUserId(updatedItinerary.tripId, userId).collect { tripEntity ->
                if (tripEntity != null) {
                    itineraryItemDao.updateItineraryItem(updatedItinerary)
                }
            }
        }
    }

    fun getItinerariesByTripId(tripId: Long): Flow<List<ItineraryItemEntity>> {
        val userId = _currentUserId.value
        return if (userId != null) {
            tripDao.getTripByIdAndUserId(tripId, userId)
                .combine(itineraryItemDao.getItinerariesByTripId(tripId)) { trip, itineraries ->
                    if (trip != null) itineraries else emptyList()
                }
        } else {
            MutableStateFlow(emptyList())
        }
    }

    fun deleteItinerary(itineraryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = _currentUserId.value ?: return@launch
            itineraryItemDao.getItineraryItemById(itineraryId).collect { item ->
                item?.let {
                    tripDao.getTripByIdAndUserId(it.tripId, userId).collect { tripEntity ->
                        if (tripEntity != null) {
                            itineraryItemDao.deleteItineraryItemById(itineraryId)
                        }
                    }
                }
            }
        }
    }

    suspend fun registerUser(
        username: String,
        email: String,
        birthDate: Date,
        address: String,
        country: String,
        phoneNumber: String,
        acceptEmailsOffers: Boolean,
        firebaseUid: String? = null
    ): Long {
        return withContext(Dispatchers.IO) {
            try {
                if (firebaseUid != null) {
                    val existingUser = userDao.getUserByFirebaseUid(firebaseUid)
                    if (existingUser != null) {
                        val updatedUser = existingUser.copy(
                            username = username,
                            email = email,
                            birthDate = birthDate,
                            address = address,
                            country = country,
                            phoneNumber = phoneNumber,
                            acceptEmailsOffers = acceptEmailsOffers
                        )
                        userDao.updateUser(updatedUser)
                        _currentUser.postValue(updatedUser)
                        _currentUserId.value = updatedUser.id
                        return@withContext updatedUser.id
                    }
                }
                if (userDao.isUsernameExists(username)) {
                    throw IllegalArgumentException("El nombre de usuario ya está en uso")
                }

                val userEntity = UserEntity(
                    username = username,
                    email = email,
                    birthDate = birthDate,
                    address = address,
                    country = country,
                    phoneNumber = phoneNumber,
                    acceptEmailsOffers = acceptEmailsOffers,
                    firebaseUid = firebaseUid
                )
                val userId = userDao.insertUser(userEntity)
                if (FirebaseAuth.getInstance().currentUser?.uid == firebaseUid) {
                    _currentUser.postValue(userEntity.copy(id = userId))
                    _currentUserId.value = userId
                }
                _registrationError.value = null
                userId
            } catch (e: Exception) {
                _registrationError.value = e.message
                throw e
            }
        }
    }

    suspend fun isUsernameExists(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.isUsernameExists(username)
        }
    }

    suspend fun getUserByFirebaseUid(firebaseUid: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByFirebaseUid(firebaseUid)
        }
    }

    suspend fun updateUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                userDao.updateUser(userEntity)
                if (userEntity.id == _currentUserId.value) {
                    _currentUser.postValue(userEntity)
                }
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user: ${e.message}", e)
                false
            }
        }
    }

    // NUEVO: imágenes del viaje
    private val _tripImages = MutableLiveData<List<TripImage>>(emptyList())
    val tripImages: LiveData<List<TripImage>> = _tripImages

    fun loadImagesForTrip(tripId: Long) {
        viewModelScope.launch {
            _tripImages.value = tripImageRepo.getImages(tripId)
        }
    }

    fun saveImageForTrip(tripId: Long, uri: String) {
        viewModelScope.launch {
            tripImageRepo.saveImage(tripId, uri)
            loadImagesForTrip(tripId)
        }
    }
}
