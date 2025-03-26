package com.example.FlyHigh.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.FlyHigh.domain.model.Trip

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)

    @Query("SELECT * FROM trip_table ORDER BY start_date DESC")
    fun getAllTrips(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table WHERE trip_id = :tripId")
    suspend fun getTripById(tripId: String): Trip?

    @Delete
    suspend fun deleteTrip(trip: Trip)
}
