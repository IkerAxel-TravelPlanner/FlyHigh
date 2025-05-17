package com.example.FlyHigh.data.local.dao

import androidx.room.*
import com.example.FlyHigh.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity): Long

    @Query("SELECT * FROM reservations")
    fun getAllReservations(): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE tripId = :tripId")
    fun getReservationByTripId(tripId: Long): Flow<ReservationEntity?>

    @Delete
    suspend fun deleteReservation(reservation: ReservationEntity)
}
