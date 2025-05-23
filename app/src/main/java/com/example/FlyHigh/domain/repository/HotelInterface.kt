package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.Availability
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.domain.model.ReserveRequest
import com.example.FlyHigh.data.remote.dto.ReservationDto
import com.example.FlyHigh.domain.model.Reservation

interface HotelInterface {

    /** List all hotels of the current group. */
    suspend fun listHotels(): List<Hotel>

    /** Check availability of hotels for a specific date range. */
    suspend fun checkAvailability(
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): Availability

    /** Reserve a room. */
    suspend fun reserveRoom(reserveRequest: ReserveRequest): String?

    /** Cancel a reservation. */
    suspend fun cancelReservation(reserveRequest: ReserveRequest): Boolean

    /** Get reservations by guest email. */
    suspend fun getReservations(guestEmail: String? = null): List<Reservation>
}
