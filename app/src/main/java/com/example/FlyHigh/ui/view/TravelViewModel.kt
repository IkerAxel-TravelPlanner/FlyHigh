package com.example.FlyHigh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

// Modelo de datos para un viaje
data class Travel(val id: String, val name: String, val description: String, val itineraries: MutableList<Itinerary> = mutableListOf())

class TravelViewModel : ViewModel() {
    private var _travels = mutableStateListOf<Travel>()
    val travels: List<Travel> get() = _travels

    // Agregar un nuevo viaje
    fun addTravel(name: String, description: String) {
        val newTravel = Travel(id = System.currentTimeMillis().toString(), name = name, description = description)
        _travels.add(newTravel)
    }

    // Actualizar un viaje existente
    fun updateTravel(updatedTravel: Travel) {
        val index = _travels.indexOfFirst { it.id == updatedTravel.id }
        if (index != -1) {
            _travels[index] = updatedTravel
        }
    }

    // Eliminar un viaje
    fun deleteTravel(travelId: String) {
        _travels.removeAll { it.id == travelId }
    }

    // Agregar un itinerario a un viaje específico
    fun addItineraryToTravel(travelId: String, itinerary: Itinerary) {
        val travel = _travels.find { it.id == travelId }
        travel?.itineraries?.add(itinerary)
    }

    // Eliminar un itinerario de un viaje específico
    fun deleteItineraryFromTravel(travelId: String, itineraryId: String) {
        val travel = _travels.find { it.id == travelId }
        travel?.itineraries?.removeAll { it.id == itineraryId }
    }
}
