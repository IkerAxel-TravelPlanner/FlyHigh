package com.example.FlyHigh.data.database

import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import android.content.Context
import androidx.room.*
import com.example.FlyHigh.data.Converters
import com.example.FlyHigh.data.local.dao.UserDao
import com.example.FlyHigh.domain.model.*

@Database(entities = [Trip::class, ItineraryItem::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flyhigh_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
