package com.example.FlyHigh.ui.viewmodel

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

    fun addTravel(name: String, description: String) {
        travels.add(Travel(id = System.currentTimeMillis().toString(), name, description))
    }

    fun updateTravel(updatedTravel: Travel) {
        val index = travels.indexOfFirst { it.id == updatedTravel.id }
        if (index != -1) travels[index] = updatedTravel
    }

    fun deleteTravel(travelId: String) {
        travels.removeAll { it.id == travelId }
    }

    fun addItinerary(travelId: String, name: String, description: String) {
        travels.find { it.id == travelId }?.itineraries?.add(
            Itinerary(id = System.currentTimeMillis().toString(), name, description)
        )
    }

    fun updateItinerary(travelId: String, updatedItinerary: Itinerary) {
        travels.find { it.id == travelId }?.itineraries?.let { itineraries ->
            val index = itineraries.indexOfFirst { it.id == updatedItinerary.id }
            if (index != -1) itineraries[index] = updatedItinerary
        }
    }

    fun deleteItinerary(travelId: String, itineraryId: String) {
        travels.find { it.id == travelId }?.itineraries?.removeAll { it.id == itineraryId }
    }
}

