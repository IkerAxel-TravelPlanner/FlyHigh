package com.example.FlyHigh.domain.model

import ItineraryItemDTO
import com.example.FlyHigh.domain.model.ItineraryItemDTO  // Corregir la ruta del paquete

data class AIRecommendations {
    var recommendedActivities: List<ItineraryItemDTO> = listOf()
    var suggestedTrips: List<Trip> = listOf()

    fun generateRecommendations(user: User) {}
    fun analyzePastTrips(user: User) {}
}
