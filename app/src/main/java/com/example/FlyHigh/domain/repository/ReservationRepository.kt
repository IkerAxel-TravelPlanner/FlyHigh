package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.local.dao.ReservationDao
import com.example.FlyHigh.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReservationRepository @Inject constructor(
    private val dao: ReservationDao
) {
    fun getAllReservations(): Flow<List<ReservationEntity>> = dao.getAllReservations()
    fun getReservationByTripId(tripId: Long): Flow<ReservationEntity?> = dao.getReservationByTripId(tripId)
    suspend fun insert(res: ReservationEntity): Long = dao.insertReservation(res)
    suspend fun delete(res: ReservationEntity) = dao.deleteReservation(res)
}
