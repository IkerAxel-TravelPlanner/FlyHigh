package com.example.FlyHigh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.domain.model.Availability
import com.example.FlyHigh.domain.model.ReserveRequest
import com.example.FlyHigh.domain.repository.HotelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepository: HotelInterface
) : ViewModel() {

    private val _availabilityResults = MutableStateFlow<Availability?>(null)
    val availabilityResults: StateFlow<Availability?> = _availabilityResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun checkAvailability(
        startDate: String,
        endDate: String,
        city: String? = null,
        hotelId: String? = null
    ) {
        viewModelScope.launch {
            try {
                _error.value = ""
                _isLoading.value = true

                val availability = hotelRepository.checkAvailability(
                    startDate = startDate,
                    endDate = endDate,
                    hotelId = hotelId,
                    city = city
                )

                _availabilityResults.value = availability
            } catch (e: Exception) {
                _error.value = e.message ?: "Error checking availability"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun listAllHotels() {
        viewModelScope.launch {
            try {
                _error.value = ""
                _isLoading.value = true

                val hotels = hotelRepository.listHotels()
                _availabilityResults.value = Availability(hotels = hotels)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching hotels"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun reserveRoom(reserveRequest: ReserveRequest): String? {
        return try {
            _error.value = ""
            hotelRepository.reserveRoom(reserveRequest)
        } catch (e: Exception) {
            // Detectar específicamente errores HTTP 409 Conflict
            if (e is HttpException && e.code() == 409) {
                _error.value = "This room is no longer available for the selected dates. Please try another room or different dates."
            } else {
                _error.value = e.message ?: "Error reserving room"
            }
            null
        }
    }

    suspend fun cancelReservation(reserveRequest: ReserveRequest): Boolean {
        return try {
            _error.value = ""
            hotelRepository.cancelReservation(reserveRequest)
        } catch (e: Exception) {
            _error.value = e.message ?: "Error canceling reservation"
            false
        }
    }
}