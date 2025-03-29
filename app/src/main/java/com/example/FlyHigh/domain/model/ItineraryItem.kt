package com.example.FlyHigh.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "itinerary_items")
data class ItineraryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long,
    val title: String,
    val description: String,
    val location: String,
    val date: Date,
    val startTime: Date?,
    val endTime: Date?,
    val type: String
)
