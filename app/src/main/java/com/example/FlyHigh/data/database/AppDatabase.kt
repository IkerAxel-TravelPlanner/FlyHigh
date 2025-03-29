package com.example.FlyHigh.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.FlyHigh.data.local.converters.DateConverter
import com.example.FlyHigh.data.local.converters.MapConverter
import com.example.FlyHigh.data.local.converters.UserConverter
import com.example.FlyHigh.data.local.converters.ItineraryItemListConverter
import com.example.FlyHigh.data.local.converters.ImageListConverter
import com.example.FlyHigh.data.local.converters.AIRecommendationsListConverter
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.data.local.entity.TripEntity


@Database(
    entities = [TripEntity::class, ItineraryItemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class) // <-- Agrega esta línea
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryDao(): ItineraryItemDao
}
