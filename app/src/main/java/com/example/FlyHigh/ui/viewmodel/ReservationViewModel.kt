package com.example.FlyHigh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.data.local.entity.ReservationEntity
import com.example.FlyHigh.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val repository: ReservationRepository
) : ViewModel() {

    val allReservations: Flow<List<ReservationEntity>> = repository.getAllReservations()

    fun getReservationByTripId(tripId: Long): Flow<ReservationEntity?> {
        return repository.getReservationByTripId(tripId)
    }

    fun addReservation(reservation: ReservationEntity) {
        viewModelScope.launch {
            repository.insert(reservation)
        }
    }

    fun deleteReservation(reservation: ReservationEntity) {
        viewModelScope.launch {
            // Simulación de borrado en API (si aplica)
            repository.delete(reservation)
        }
    }

    }

