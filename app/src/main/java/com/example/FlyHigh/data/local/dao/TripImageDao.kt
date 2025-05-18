package com.example.FlyHigh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.FlyHigh.data.local.entity.TripImageEntity

@Dao
interface TripImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: TripImageEntity): Long

    @Query("SELECT * FROM trip_images WHERE tripId = :tripId")
    suspend fun getImagesByTripId(tripId: Long): List<TripImageEntity>

    @Query("DELETE FROM trip_images WHERE tripId = :tripId AND uri = :uri")
    suspend fun deleteImageByTripIdAndUri(tripId: Long, uri: String)

}