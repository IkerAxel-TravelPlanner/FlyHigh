//No nos dejaba añadir la libreria de HILT, y hemos hecho un apaño, hemos implementado las funciones aquí directamente.



package com.example.FlyHigh.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.TravelRepository
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.domain.model.ItineraryItem
import com.example.FlyHigh.domain.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

// Modelo de datos para un itinerario
data class Itinerary(val id: String, val name: String, val description: String)

// Modelo de datos para un viaje
data class Travel(
    val id: String,
    val name: String,
    val description: String,
    val itineraries: MutableList<Itinerary> = mutableStateListOf()
)

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {
    private val _travels = MutableStateFlow<List<Travel>>(emptyList())
    val travels: StateFlow<List<Travel>> = _travels
    private val TAG = "TravelViewModel"  // Etiqueta para logs

    init {
        loadTravels()
    }

    private fun loadTravels() {
        viewModelScope.launch {
            repository.getAllTrips().collectLatest { travels ->
                _travels.value = travels
            }
        }
    }

    // Agregar un viaje
    fun addTravel(name: String, description: String) {
        Log.i(TAG, "Adding travel: $name")
        viewModelScope.launch {
            // Crear un nuevo viaje y agregarlo a la lista de viajes
            repository.insertTrip(
                Trip(
                    name = name,
                    startDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now(),
                    destination = description
                )
            )
        }
        Log.i(TAG, "Travel added successfully: $name")
    }

    // Actualizar un viaje existente
    fun updateTravel(updatedTravel: Travel) {
        Log.i(TAG, "Updating travel: ${updatedTravel.name}")
        viewModelScope.launch {
            repository.updateTrip(
                Trip(
                    id = updatedTravel.id.toInt(),
                    name = updatedTravel.name,
                    startDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now(),
                    destination = updatedTravel.description
                )
            )
        }
        Log.i(TAG, "Travel updated successfully: ${updatedTravel.name}")
    }

    // Eliminar un viaje
    fun deleteTravel(travelId: String) {
        Log.i(TAG, "Deleting travel with ID: $travelId")
        viewModelScope.launch {
            repository.deleteTrip(Trip(id = travelId.toInt(), name = "", startDate = LocalDateTime.now(), endDate = LocalDateTime.now(), destination = ""))
        }
        Log.i(TAG, "Travel deleted successfully with ID: $travelId")
    }

    // Agregar un itinerario a un viaje
    fun addItinerary(travelId: String, name: String, description: String) {
        Log.i(TAG, "Adding itinerary: $name to travel with ID: $travelId")
        viewModelScope.launch {
            repository.insertItineraryItem(
                ItineraryItem(
                    tripId = travelId.toInt(),
                    description = name,
                    dateTime = LocalDateTime.now(),
                    location = description,
                    priority = 1
                )
            )
        }
        Log.i(TAG, "Itinerary added successfully: $name to travel: $travelId")
    }


    // Actualizar un itinerario en un viaje
    fun updateItinerary(travelId: String, updatedItinerary: Itinerary) {
        Log.i(TAG, "Updating itinerary: ${updatedItinerary.name} for travel with ID: $travelId")

        val travel = travels.find { it.id == travelId }
        travel?.itineraries?.let { itineraries ->
            val index = itineraries.indexOfFirst { it.id == updatedItinerary.id }
            if (index != -1) {
                itineraries[index] = updatedItinerary
                Log.i(TAG, "Itinerary updated successfully: ${updatedItinerary.name}")
            } else {
                Log.e(TAG, "Failed to update itinerary: ${updatedItinerary.name} (not found)")
            }
        } ?: run {
            Log.e(TAG, "Failed to update itinerary: ${updatedItinerary.name} (travel not found)")
        }
    }

    // Eliminar un itinerario de un viaje
    fun deleteItinerary(travelId: String, itineraryId: String) {
        Log.i(TAG, "Deleting itinerary with ID: $itineraryId from travel with ID: $travelId")

        val travel = travels.find { it.id == travelId }
        travel?.itineraries?.removeAll { it.id == itineraryId }

        if (travel?.itineraries?.none { it.id == itineraryId } == true) {
            Log.i(TAG, "Itinerary deleted successfully with ID: $itineraryId")
        } else {
            Log.e(TAG, "Failed to delete itinerary with ID: $itineraryId (not found)")
        }
    }
}
