package com.example.FlyHigh.domain.model

import android.health.connect.datatypes.ExerciseRoute
import com.google.android.libraries.mapsplatform.transportation.consumer.model.Route

class Map {
    fun showLocation(latitude: Double, longitude: Double) {}
    fun getNearbyPlaces(category: String): List<String> = listOf()
    fun getDirections(start: ExerciseRoute.Location, end: ExerciseRoute.Location): Route? = null
}