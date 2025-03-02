package com.example.navigation.modules

import android.media.Image
import java.util.Date

class Trip(
    var tripId: String,
    var name: String,
    var startDate: Date,
    var endDate: Date,
    var destination: String,
    var itinerary: List<ItineraryItem>,
    var photos: List<Image>
) {
    fun addItineraryItem(item: ItineraryItem) {}
    fun removeItineraryItem(item: ItineraryItem) {}
    fun uploadPhoto(img: Image) {}
    fun shareTrip(user: User) {}
}