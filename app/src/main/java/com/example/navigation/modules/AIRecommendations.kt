package com.example.navigation.modules

class AIRecommendations {
    var recommendedActivities: List<ItineraryItem> = listOf()
    var suggestedTrips: List<Trip> = listOf()

    fun generateRecommendations(user: User) {}
    fun analyzePastTrips(user: User) {}
}