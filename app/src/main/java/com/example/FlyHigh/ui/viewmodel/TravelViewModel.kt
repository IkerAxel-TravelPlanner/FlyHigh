package com.example.FlyHigh.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

// Modelo de datos para un itinerario
data class Itinerary(val id: String, val name: String, val description: String)

// Modelo de datos para un viaje
data class Travel(
    val id: String,
    val name: String,
    val description: String,
    val itineraries: MutableList<Itinerary> = mutableStateListOf()
)

class TravelViewModel : ViewModel() {
    var travels = mutableStateListOf<Travel>()
    private val TAG = "TravelViewModel"  // Etiqueta para logs

    // Agregar un viaje
    fun addTravel(name: String, description: String) {
        Log.i(TAG, "Adding travel: $name")

        // Crear un nuevo viaje y agregarlo a la lista de viajes
        travels.add(Travel(id = System.currentTimeMillis().toString(), name, description))

        Log.i(TAG, "Travel added successfully: $name")
    }

    // Actualizar un viaje existente
    fun updateTravel(updatedTravel: Travel) {
        Log.i(TAG, "Updating travel: ${updatedTravel.name}")

        val index = travels.indexOfFirst { it.id == updatedTravel.id }
        if (index != -1) {
            travels[index] = updatedTravel
            Log.i(TAG, "Travel updated successfully: ${updatedTravel.name}")
        } else {
            Log.e(TAG, "Failed to update travel: ${updatedTravel.name} (not found)")
        }
    }

    // Eliminar un viaje
    fun deleteTravel(travelId: String) {
        Log.i(TAG, "Deleting travel with ID: $travelId")

        val removed = travels.removeAll { it.id == travelId }

        if (removed) {
            Log.i(TAG, "Travel deleted successfully with ID: $travelId")
        } else {
            Log.e(TAG, "Failed to delete travel with ID: $travelId (not found)")
        }
    }

    // Agregar un itinerario a un viaje
    fun addItinerary(travelId: String, name: String, description: String) {
        Log.i(TAG, "Adding itinerary: $name to travel with ID: $travelId")

        val travel = travels.find { it.id == travelId }
        travel?.itineraries?.add(
            Itinerary(id = System.currentTimeMillis().toString(), name, description)
        )

        travel?.let {
            Log.i(TAG, "Itinerary added successfully: $name to travel: ${it.name}")
        } ?: run {
            Log.e(TAG, "Failed to add itinerary: $name (travel not found)")
        }
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
