package com.example.FlyHigh.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ItineraryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItem(itineraryItem: ItineraryItemEntity)

    @Update
    suspend fun updateItineraryItem(itineraryItem: ItineraryItemEntity)

    @Query("DELETE FROM itinerary_items WHERE id = :itineraryId")
    suspend fun deleteItineraryItemById(itineraryId: Long)

    @Query("SELECT * FROM itinerary_items WHERE id = :itineraryId")
    fun getItineraryItemById(itineraryId: Long): Flow<ItineraryItemEntity?>

    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId")
    fun getItinerariesByTripId(tripId: Long): Flow<List<ItineraryItemEntity>>

    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId ORDER BY startTime ASC")
    fun getItemsByTrip(tripId: Long): LiveData<List<ItineraryItemEntity>>
}


