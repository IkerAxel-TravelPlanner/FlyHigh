package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.*
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
        // Crear el objeto Trip con las nuevas propiedades
        val newTrip = Trip(
            map = null, // Mapa opcional, puede asignarse posteriormente
            id = tripId,
            user = null, // Usuario opcional, se puede asignar más tarde
            destination = destination,
            itineraries = emptyList(),
            startDate = startDate.toString(),
            endDate = endDate.toString(),
            images = emptyList(),
            aiRecommendations = emptyList(),
            imageURL = "https://example.com/default-trip-image.jpg"
        )

        // Simular la persistencia: agregar el nuevo viaje a la "base de datos" en memoria
        val isAdded = tripList.add(newTrip)

        if (!isAdded) {
            throw IllegalArgumentException("No se ha podido añadir el trip: $name")
        }

        return newTrip
    }

    override fun editTrip(
        tripId: String,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String
    ): Trip {
        val tripToEdit = tripList.find { it.id == tripId }
        return tripToEdit?.let {
            it.copy(
                destination = destination,
                startDate = startDate.toString(),
                endDate = endDate.toString()
            ).also { updatedTrip ->
                tripList[tripList.indexOf(tripToEdit)] = updatedTrip
            }
        } ?: throw IllegalArgumentException("No se encontró el viaje con tripId: $tripId")
    }

    override fun deleteTrip(tripId: String) {
        val removed = tripList.removeIf { it.id == tripId }
        if (!removed) {
            throw IllegalArgumentException("No se encontró un viaje con tripId: $tripId")
        }
    }
}
