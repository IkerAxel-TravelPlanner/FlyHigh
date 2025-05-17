package com.example.FlyHigh.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long,
    val hotelName: String,
    val roomType: String,
    val imageUrl: String?, // imagen del hotel o habitación
    val checkIn: String,
    val checkOut: String,
    val reservationId: String
)
