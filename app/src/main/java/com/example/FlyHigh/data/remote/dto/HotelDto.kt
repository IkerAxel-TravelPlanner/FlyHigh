package com.example.FlyHigh.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<RoomDto>,
    @SerializedName("image_url") val imageUrl: String
)

data class RoomDto(
    val id: String,
    @SerializedName("room_type") val roomType: String,
    val price: Double,
    val images: List<String>
)

data class AvailabilityResponseDto(
    @SerializedName("available_hotels") val hotels: List<HotelDto>
)

data class ReserveRequestDto(
    @SerializedName("hotel_id") val hotelId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("guest_name") val guestName: String,
    @SerializedName("guest_email") val guestEmail: String
)

data class ReservationResponseDto(
    val ok: Boolean,
    val message: String,
    @SerializedName("reservation_id") val reservationId: String?

)

data class ReservationDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("hotel_id")
    val hotelId: String,
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("guest_name")
    val guestName: String,
    @SerializedName("guest_email")
    val guestEmail: String
)

data class ReservationResponseBody(
    val message: String,
    val nigths: Int,
    val reservation: ReservationDto
)