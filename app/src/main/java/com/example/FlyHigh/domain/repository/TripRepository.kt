package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.Trip
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor() : TripInterface {

    // Simulación de una "base de datos" en memoria
    private val tripList = mutableListOf<Trip>()

    override fun addTrip(
        tripId: String,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String
    ): Trip {
        // Crear el objeto Trip, usando listas vacías para itinerario y fotos
        val newTrip = Trip(tripId, name, startDate, endDate, destination, emptyList(), emptyList())

        // Simular la persistencia: agregar el nuevo viaje a la "base de datos" en memoria
        val isAdded = tripList.add(newTrip)

        if (!isAdded){
            throw IllegalArgumentException("No se ha podido añadir el trip: $name")
        }

        // Retornar el viaje recién agregado
        return newTrip
    }

    override fun editTrip(
        tripId: String,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String
    ): Trip {
        val tripToEdit = tripList.find { it.tripId == tripId }
        tripToEdit?.let {
            it.name = name
            it.startDate = startDate
            it.endDate = endDate
            it.destination = destination
            // Aquí podrías actualizar otros campos, si fuera necesario.
        }
        return tripToEdit ?: throw IllegalArgumentException("No se encontró el viaje con tripId: $tripId")
    }

    override fun deleteTrip(tripId: String) {
        val removed = tripList.removeIf { it.tripId == tripId }
        if (!removed) {
            throw IllegalArgumentException("No se encontró un viaje con tripId: $tripId")
        }
    }
}
