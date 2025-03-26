package com.example.FlyHigh.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.FlyHigh.domain.model.ItineraryItem


@Dao
interface ItineraryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(itineraryItem: ItineraryItem)

    @Query("SELECT * FROM itinerary_table WHERE trip_id = :tripId ORDER BY start_time ASC")
    fun getItemsByTrip(tripId: String): LiveData<List<ItineraryItem>>

    @Delete
    suspend fun deleteItem(itineraryItem: ItineraryItem)
}
