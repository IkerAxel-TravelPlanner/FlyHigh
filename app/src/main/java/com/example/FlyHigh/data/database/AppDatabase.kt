package com.example.FlyHigh.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.FlyHigh.data.Converters
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.domain.model.ItineraryItem
import com.example.FlyHigh.domain.model.Trip

@Database(entities = [Trip::class, ItineraryItem::class], version = 1)
@TypeConverters(Converters::class) // This is the correct one //  //
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao
}

