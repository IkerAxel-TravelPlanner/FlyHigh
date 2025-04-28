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
import com.example.FlyHigh.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel para gestionar la lógica de negocio relacionada con los viajes y los itinerarios.
 * Proporciona métodos para interactuar con la base de datos a través de los DAOs.
 */
@HiltViewModel
class TravelViewModel @Inject constructor(
    private val tripDao: TripDao,
    private val itineraryItemDao: ItineraryItemDao,
    private val userDao: UserDao
) : ViewModel() {

    private val TAG = "TravelViewModel"

    private val _currentUser = MutableLiveData<UserEntity?>()
    val currentUser: LiveData<UserEntity?> = _currentUser

    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    private val _tripsRefreshTrigger = MutableStateFlow(0)
    val tripsRefreshTrigger: StateFlow<Int> = _tripsRefreshTrigger.asStateFlow()

    // Agregamos un estado para manejar errores en el registro
    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError.asStateFlow()


    // Agregamos un listener para los cambios de autenticación
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
                    // Buscar el usuario en Room por su firebaseUid
                    var user = withContext(Dispatchers.IO) {
                        userDao.getUserByFirebaseUid(firebaseUser.uid)
                    }

                    // Si no existe, crearlo automáticamente
                    if (user == null) {
                        val userId = createLocalUserFromFirebase(firebaseUser)
                        user = withContext(Dispatchers.IO) {
                            userDao.getUserByIdSuspend(userId)
                        }
                    }

                    _currentUser.value = user
                    _currentUserId.value = user?.id
                    refreshTrips()

                    Log.d(TAG, "User loaded: ${user?.username} with ID ${user?.id}")
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

    // Método para forzar la actualización de la lista de viajes
    fun refreshTrips() {
        _tripsRefreshTrigger.value = _tripsRefreshTrigger.value + 1
    }

    fun getTripById(tripId: Long): Flow<TripEntity?> {
        return if (_currentUserId.value != null) {
            tripDao.getTripByIdAndUserId(tripId, _currentUserId.value!!)
        } else {
            tripDao.getTripById(tripId)
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

    // Agregar un viaje a la base de datos
    fun addTravel(title: String, destination: String, startDate: Date, endDate: Date, description: String, imageUrl: String?) {
        viewModelScope.launch {
            try {
                // Obtén el usuario de Firebase
                val firebaseUser = FirebaseAuth.getInstance().currentUser

                if (firebaseUser == null) {
                    Log.e(TAG, "Failed to add trip: No Firebase user logged in")
                    return@launch
                }

                // Asegurarnos de que tenemos un usuario local
                var userId = _currentUserId.value

                if (userId == null) {
                    // Intentar cargar el usuario primero
                    loadCurrentUser()

                    // Esperar un momento para que se cargue
                    delay(500)

                    // Verificar si se cargó
                    userId = _currentUserId.value

                    // Si todavía no hay usuario, intentar crearlo
                    if (userId == null) {
                        val localUser = getUserByFirebaseUid(firebaseUser.uid)
                        if (localUser != null) {
                            userId = localUser.id
                        } else {
                            userId = createLocalUserFromFirebase(firebaseUser)
                        }
                    }
                }

                // Verificar que tenemos un userId válido
                if (userId == null || userId <= 0) {
                    Log.e(TAG, "Failed to add trip: Invalid user ID")
                    return@launch
                }

                // A partir de aquí, usar el userId para crear el viaje
                val trip = TripEntity(
                    userId = userId,
                    title = title,
                    destination = destination,
                    startDate = startDate,
                    endDate = endDate,
                    description = description,
                    imageUrl = imageUrl
                )

                val tripId = withContext(Dispatchers.IO) {
                    tripDao.insertTrip(trip)
                }

                Log.i(TAG, "Travel added successfully: $title with ID $tripId for user $userId")
                refreshTrips()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding travel: ${e.message}", e)
            }
        }
    }


    // Actualizar un viaje existente
    fun updateTravel(updatedTrip: TripEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // Asegurarse de que el usuario actual es el propietario del viaje
            if (_currentUserId.value != null && updatedTrip.userId == _currentUserId.value) {
                tripDao.updateTrip(updatedTrip)
                Log.i(TAG, "Travel updated successfully: ${updatedTrip.title}")

                // Forzar actualización después de modificar
                refreshTrips()
            } else {
                Log.e(TAG, "Failed to update trip: Permission denied")
            }
        }
    }

    // Eliminar un viaje
    fun deleteTravel(tripId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // Comprobar propiedad antes de eliminar
            tripDao.getTripById(tripId).collect { trip ->
                if (trip != null && (_currentUserId.value == null || trip.userId == _currentUserId.value)) {
                    tripDao.deleteTripById(tripId)
                    Log.i(TAG, "Travel deleted successfully with ID: $tripId")

                    // Forzar actualización después de eliminar
                    refreshTrips()
                } else {
                    Log.e(TAG, "Failed to delete trip: Permission denied or trip not found")
                }
            }
        }
    }

    fun getItineraryById(itineraryId: Long): Flow<ItineraryItemEntity?> {
        return itineraryItemDao.getItineraryItemById(itineraryId)
    }

    // Agregar un itinerario a un viaje
    fun addItinerary(tripId: Long, title: String, description: String, location: String, date: Date, startTime: Date?, endTime: Date?, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Verificar que el viaje pertenece al usuario actual
            val trip = tripDao.getTripById(tripId)
            trip.collect { tripEntity ->
                if (tripEntity != null && (_currentUserId.value == null || tripEntity.userId == _currentUserId.value)) {
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
                    Log.i(TAG, "Itinerary added successfully: $title")
                } else {
                    Log.e(TAG, "Failed to add itinerary: Permission denied or trip not found")
                }
            }
        }
    }

    // Actualizar un itinerario en un viaje
    fun updateItinerary(updatedItinerary: ItineraryItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // Verificar que el itinerario pertenece a un viaje del usuario actual
            val trip = tripDao.getTripById(updatedItinerary.tripId)
            trip.collect { tripEntity ->
                if (tripEntity != null && (_currentUserId.value == null || tripEntity.userId == _currentUserId.value)) {
                    itineraryItemDao.updateItineraryItem(updatedItinerary)
                    Log.i(TAG, "Itinerary updated successfully: ${updatedItinerary.title}")
                } else {
                    Log.e(TAG, "Failed to update itinerary: Permission denied or trip not found")
                }
            }
        }
    }

    fun getItinerariesByTripId(tripId: Long): Flow<List<ItineraryItemEntity>> {
        return itineraryItemDao.getItinerariesByTripId(tripId)
    }

    // Eliminar un itinerario de un viaje
    fun deleteItinerary(itineraryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // Opcional: verificar que el itinerario pertenece a un viaje del usuario actual
            val itinerary = itineraryItemDao.getItineraryItemById(itineraryId)
            itinerary.collect { item ->
                if (item != null) {
                    val trip = tripDao.getTripById(item.tripId)
                    trip.collect { tripEntity ->
                        if (tripEntity != null && (_currentUserId.value == null || tripEntity.userId == _currentUserId.value)) {
                            itineraryItemDao.deleteItineraryItemById(itineraryId)
                            Log.i(TAG, "Itinerary deleted successfully with ID: $itineraryId")
                        } else {
                            Log.e(TAG, "Failed to delete itinerary: Permission denied or trip not found")
                        }
                    }
                }
            }
        }
    }

    // Métodos de gestión de usuarios
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
                // Verificar si el usuario ya existe por firebaseUid
                if (firebaseUid != null) {
                    val existingUser = userDao.getUserByFirebaseUid(firebaseUid)
                    if (existingUser != null) {
                        // Actualizar el usuario existente en lugar de crear uno nuevo
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

                // Comprobar si el nombre de usuario ya existe (solo si es un usuario nuevo)
                if (userDao.isUsernameExists(username)) {
                    throw IllegalArgumentException("El nombre de usuario ya está en uso")
                }

                // Crear nuevo usuario
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

                // Actualizar el usuario actual si corresponde
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

                // Si es el usuario actual, actualizar el estado
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


}