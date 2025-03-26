package com.example.FlyHigh.domain.model

import android.health.connect.datatypes.ExerciseRoute
import androidx.room.*
import java.util.Date

@Entity(
    tableName = "itinerary_table",
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = ["trip_id"],
        childColumns = ["trip_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ItineraryItem(
    @PrimaryKey @ColumnInfo(name = "item_id") val itemId: String,
    @ColumnInfo(name = "trip_id") val tripId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "start_time") val startTime: Date,
    @ColumnInfo(name = "end_time") val endTime: ExerciseRoute.Location,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
)
