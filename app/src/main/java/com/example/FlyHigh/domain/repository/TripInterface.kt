package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.Trip
import java.util.Date

interface TripInterface {
    fun addTrip(tripId: String, name: String, startDate: Date, endDate: Date, destination: String): Trip
    fun editTrip(tripId: String, name: String, startDate: Date, endDate: Date, destination: String): Trip
    fun deleteTrip(tripId: String)
}
