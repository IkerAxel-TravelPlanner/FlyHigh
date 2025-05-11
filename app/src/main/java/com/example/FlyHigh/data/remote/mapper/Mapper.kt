package com.example.FlyHigh.data.remote.mapper

import com.example.FlyHigh.data.remote.dto.HotelDto
import com.example.FlyHigh.data.remote.dto.RoomDto
import com.example.FlyHigh.data.remote.dto.ReserveRequestDto
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.domain.model.Room
import com.example.FlyHigh.domain.model.ReserveRequest

// mapper for Hotel
fun HotelDto.toDomain() = Hotel(
    id = id,
    name = name,
    address = address,
    rating = rating,
    rooms = rooms.map { it.toDomain() },
    imageUrl = imageUrl
)

// mapper for Room
fun RoomDto.toDomain() = Room(
    id = id,
    roomType = roomType,
    price = price,
    images = images
)

// mapper for ReserveRequest
fun ReserveRequest.toDto() = ReserveRequestDto(
    hotelId = hotelId,
    roomId = roomId,
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail
)