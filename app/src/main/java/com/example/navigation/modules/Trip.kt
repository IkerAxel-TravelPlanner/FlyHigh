package com.example.navigation.modules

import android.media.Image
import java.util.Date

data class Trip(
    var tripId: String,
    var name: String,
    var startDate: Date,
    var endDate: Date,
    var destination: String,
    var itinerary: List<ItineraryItem> = listOf(),
    var photos: List<Image> = listOf()
)
