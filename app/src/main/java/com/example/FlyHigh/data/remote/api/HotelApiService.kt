package com.example.FlyHigh.data.remote.api

import com.example.FlyHigh.data.remote.dto.AvailabilityResponseDto
import com.example.FlyHigh.data.remote.dto.HotelDto
import com.example.FlyHigh.data.remote.dto.ReservationResponseDto
import com.example.FlyHigh.data.remote.dto.ReserveRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApiService {

    @GET("hotels/{group_id}/hotels")
    suspend fun getHotels(@Path("group_id") groupId: String): List<HotelDto>

    @GET("hotels/{group_id}/availability")
    suspend fun getHotelAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("hotel_id") hotelId: String? = null,
        @Query("city") city: String? = null
    ): AvailabilityResponseDto

    @POST("hotels/{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body reserveRequest: ReserveRequestDto
    ): ReservationResponseDto

    @POST("hotels/{group_id}/cancel")
    suspend fun cancelReservation(
        @Path("group_id") groupId: String,
        @Body reserveRequest: ReserveRequestDto
    ): ReservationResponseDto

    @GET("hotels/{group_id}/reservations")
    suspend fun getReservations(
        @Path("group_id") groupId: String,
        @Query("guest_email") guestEmail: String? = null
    ): ReservationResponseDto
}