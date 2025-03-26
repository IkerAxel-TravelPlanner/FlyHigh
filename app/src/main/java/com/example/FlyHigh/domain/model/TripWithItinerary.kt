package com.example.FlyHigh.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithItinerary(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "trip_id",
        entityColumn = "trip_id"
    )
    val itinerary: List<ItineraryItem>
)
