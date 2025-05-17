package com.example.FlyHigh.data.remote.mapper

import com.example.FlyHigh.data.remote.dto.ReservationDto
import com.example.FlyHigh.domain.model.Reservation

fun ReservationDto.toDomain(): Reservation = Reservation(
    id = id ?: "",
    hotelId = hotelId,
    roomId = roomId,
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail
)
