package com.example.FlyHigh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.data.local.entity.TripEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel para gestionar la lógica de negocio relacionada con los viajes y los itinerarios.
 * Proporciona métodos para interactuar con la base de datos a través de los DAOs.
 *
 * @param tripDao DAO para acceder a los datos de los viajes.
 * @param itineraryItemDao DAO para acceder a los datos de los itinerarios.
 */

class TravelViewModel(
    private val tripDao: TripDao,
    private val itineraryItemDao: ItineraryItemDao
) : ViewModel() {

    private val TAG = "TravelViewModel"  // Etiqueta para logs

    fun getTripById(tripId: Long): Flow<TripEntity?> {
        return tripDao.getTripById(tripId)
    }

    fun getAllTrips(): Flow<List<TripEntity>> {
        return tripDao.getAllTrips().asFlow()  // Convertimos LiveData a Flow
    }

    // Agregar un viaje a la base de datos
    fun addTravel(title: String, destination: String, startDate: Date, endDate: Date, description: String, imageUrl: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val trip = TripEntity(
                title = title,
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                description = description,
                imageUrl = imageUrl
            )
            tripDao.insertTrip(trip)
            Log.i(TAG, "Travel added successfully: $title")
        }
    }

    // Actualizar un viaje existente
    fun updateTravel(updatedTrip: TripEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            tripDao.updateTrip(updatedTrip)
            Log.i(TAG, "Travel updated successfully: ${updatedTrip.title}")
        }
    }

    // Eliminar un viaje
    fun deleteTravel(tripId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tripDao.deleteTripById(tripId)
            Log.i(TAG, "Travel deleted successfully with ID: $tripId")
        }
    }

    fun getItineraryById(itineraryId: Long): Flow<ItineraryItemEntity?> {
        return itineraryItemDao.getItineraryItemById(itineraryId)
    }

    // Agregar un itinerario a un viaje
    fun addItinerary(tripId: Long, title: String, description: String, location: String, date: Date, startTime: Date?, endTime: Date?, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
        }
    }

    // Actualizar un itinerario en un viaje
    fun updateItinerary(updatedItinerary: ItineraryItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            itineraryItemDao.updateItineraryItem(updatedItinerary)
            Log.i(TAG, "Itinerary updated successfully: ${updatedItinerary.title}")
        }
    }

    fun getItinerariesByTripId(tripId: Long): Flow<List<ItineraryItemEntity>> {
        return itineraryItemDao.getItinerariesByTripId(tripId)
    }

    // Eliminar un itinerario de un viaje
    fun deleteItinerary(itineraryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            itineraryItemDao.deleteItineraryItemById(itineraryId)
            Log.i(TAG, "Itinerary deleted successfully with ID: $itineraryId")
        }
    }
}

class TravelViewModelFactory(
    private val tripDao: TripDao,
    private val itineraryItemDao: ItineraryItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            return TravelViewModel(tripDao, itineraryItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
