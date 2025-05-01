package com.example.FlyHigh.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.FlyHigh.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :tripId AND userId = :userId")
    suspend fun deleteTripById(tripId: Long, userId: Long)

    // Esta función se mantiene por compatibilidad pero debería evitarse
    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTripById(tripId: Long)

    // Esta función se mantiene por compatibilidad pero debería evitarse
    @Query("SELECT * FROM trips ORDER BY startDate DESC")
    fun getAllTrips(): LiveData<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate DESC")
    fun getTripsByUserId(userId: Long): LiveData<List<TripEntity>>

    // Esta función se mantiene por compatibilidad pero debería evitarse
    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripById(tripId: Long): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE userId = :userId AND id = :tripId")
    fun getTripByIdAndUserId(tripId: Long, userId: Long): Flow<TripEntity?>
}