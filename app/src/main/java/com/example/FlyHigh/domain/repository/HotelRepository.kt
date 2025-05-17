package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.remote.api.HotelApiService
import com.example.FlyHigh.data.remote.dto.ReservationDto
import com.example.FlyHigh.data.remote.mapper.toDomain
import com.example.FlyHigh.data.remote.mapper.toDto
import com.example.FlyHigh.domain.model.Availability
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.domain.model.Reservation
import com.example.FlyHigh.domain.model.ReserveRequest
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

import com.example.FlyHigh.data.remote.mapper.toDomain

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
        try {
            val response = api.reserveRoom(gid, reserveRequest.toDto())
            return response.reservation.id
        } catch (e: HttpException) {
            // Propagar el error original para que se pueda manejar adecuadamente en el ViewModel
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to reserve room: ${e.message}")
        }
    }

    override suspend fun cancelReservation(reserveRequest: ReserveRequest): Boolean {
        try {
            val response = api.cancelReservation(gid, reserveRequest.toDto())
            return response.ok
        } catch (e: HttpException) {
            // Propagar el error original para que se pueda manejar adecuadamente en el ViewModel
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to cancel reservation: ${e.message}")
        }
    }

    override suspend fun getReservations(guestEmail: String?): List<Reservation> {
        return try {
            val response = api.getReservations(gid, guestEmail)
            response.map { it.toDomain() }
        } catch (e: HttpException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to get reservations: ${e.message}")
        }
    }

}

//FUNCIONA