package com.example.navigation.repository
import android.app.Activity
import android.health.connect.datatypes.ExerciseRoute
import com.example.navigation.modules.ItineraryItem
import com.example.navigation.modules.Trip
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

// Simulación de una "base de datos" en memoria
private val ActivityList = mutableListOf<Trip>()


class ItineraryItemRepository @Inject constructor() : ItineraryItemInterface {
    override fun addActivity(
        itemId: String,
        title: String,
        description: String,
        startTime: Date,
        endTime: Date,
        location: ExerciseRoute.Location
    ): ItineraryItem {
        // Crear el objeto Trip, usando listas vacías para itinerario y fotos
        val newActivity=  ItineraryItem(itemId,title,description,startTime,endTime,location)

        // Simular la persistencia: agregar el nuevo viaje a la "base de datos" en memoria
        val isAdded = ActivityList.add(newActivity)

        if (!isAdded){
            throw IllegalArgumentException("No se ha podido añadir el trip: $name")
        }

        // Retornar el viaje recién agregado
        return newActivity
    }

    override fun updateActivity(
        itemId: String,
        title: String,
        description: String,
        startTime: Date,
        endTime: Date,
        location: ExerciseRoute.Location
    ): ItineraryItem {
        TODO("Not yet implemented")
    }

    override fun deleteActivity(itemId: String) {
        TODO("Not yet implemented")
    }
}