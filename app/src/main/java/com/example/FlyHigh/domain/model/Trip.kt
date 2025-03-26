package com.example.FlyHigh.domain.model

import androidx.room.*
import java.util.*

@Entity(tableName = "trip_table")
data class Trip(
    @PrimaryKey @ColumnInfo(name = "trip_id") val tripId: String,
    @ColumnInfo(name = "trip_name") var name: String,
    @ColumnInfo(name = "start_date") var startDate: Date,
    @ColumnInfo(name = "end_date") var endDate: Date,
    @ColumnInfo(name = "destination") var destination: String,
    @ColumnInfo(name = "photos") val photos: List<String> = listOf(),
    val emptyList: List<Any>
)
