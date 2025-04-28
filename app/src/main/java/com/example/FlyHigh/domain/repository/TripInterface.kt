package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.Trip
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TripInterface {
    suspend fun addTrip(
        userId: Long,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String,
        description: String,
        imageUrl: String? = null
    ): Long

    suspend fun editTrip(
        tripId: Long,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String,
        description: String,
        imageUrl: String? = null
    )

    suspend fun deleteTrip(tripId: Long)

    fun getTripsForUser(userId: Long): Flow<List<Trip>>

    fun getTripById(tripId: Long): Flow<Trip?>
}