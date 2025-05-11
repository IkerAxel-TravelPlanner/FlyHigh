package com.example.FlyHigh.domain.model

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<Room>,
    val imageUrl: String
)

data class Room(
    val id: String,
    val roomType: String,
    val price: Double,
    val images: List<String>
)

data class ReserveRequest(
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String
)

data class Availability(
    val hotels: List<Hotel>
)