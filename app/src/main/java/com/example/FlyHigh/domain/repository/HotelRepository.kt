package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.remote.api.HotelApiService
import com.example.FlyHigh.data.remote.mapper.toDomain
import com.example.FlyHigh.data.remote.mapper.toDto
import com.example.FlyHigh.domain.model.Availability
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.domain.model.ReserveRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepository @Inject constructor(
    private val api: HotelApiService
) : HotelInterface {

    private val gid = "G09"                       // hard-coded group

    override suspend fun listHotels(): List<Hotel> =
        api.getHotels(gid).map { it.toDomain() }

    override suspend fun checkAvailability(
        startDate: String,
        endDate: String,
        hotelId: String?,
        city: String?
    ): Availability {
        val response = api.getHotelAvailability(gid, startDate, endDate, hotelId, city)
        return Availability(hotels = response.hotels.map { it.toDomain() })
    }

    override suspend fun reserveRoom(reserveRequest: ReserveRequest): String? {
        val response = api.reserveRoom(gid, reserveRequest.toDto())
        return response.reservationId
    }

    override suspend fun cancelReservation(reserveRequest: ReserveRequest): Boolean {
        val response = api.cancelReservation(gid, reserveRequest.toDto())
        return response.ok
    }
}