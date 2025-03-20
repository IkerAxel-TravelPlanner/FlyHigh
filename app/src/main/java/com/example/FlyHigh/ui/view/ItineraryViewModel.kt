package com.example.FlyHigh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

data class Itinerary(val id: String, val name: String, val description: String)

data class Travel(val id: String, val name: String, val itineraries: MutableList<Itinerary> = mutableListOf())

class ItineraryViewModel : ViewModel() {
    var travels = mutableStateListOf(
        Travel("1", "Viaje a París", mutableListOf(Itinerary("1", "Día 1", "Exploración de la ciudad"))),
        Travel("2", "Aventura en Japón", mutableListOf(Itinerary("2", "Día 1", "Visita al templo Senso-ji")))
    )

    fun addTravel(travel: Travel) {
        travels.add(travel)
    }

    fun updateTravel(updatedTravel: Travel) {
        val index = travels.indexOfFirst { it.id == updatedTravel.id }
        if (index != -1) travels[index] = updatedTravel
    }

    fun addItinerary(viajeId: String, itinerary: Itinerary) {
        val viaje = travels.find { it.id == viajeId }
        viaje?.itineraries?.add(itinerary)
    }

    fun updateItinerary(viajeId: String, updatedItinerary: Itinerary) {
        val viaje = travels.find { it.id == viajeId }
        viaje?.itineraries?.let { itineraries ->
            val index = itineraries.indexOfFirst { it.id == updatedItinerary.id }
            if (index != -1) itineraries[index] = updatedItinerary
        }
    }
}
