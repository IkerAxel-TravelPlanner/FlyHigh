package com.example.FlyHigh.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.FlyHigh.data.local.converters.DateConverter
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.dao.TripImageDao
import com.example.FlyHigh.data.local.dao.UserDao
import com.example.FlyHigh.data.local.dao.ReservationDao // ✅ Nuevo DAO
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.data.local.entity.TripEntity
import com.example.FlyHigh.data.local.entity.TripImageEntity
import com.example.FlyHigh.data.local.entity.UserEntity
import com.example.FlyHigh.data.local.entity.ReservationEntity // ✅ Nueva entidad

@Database(
    entities = [
        TripEntity::class,
        ItineraryItemEntity::class,
        UserEntity::class,
        TripImageEntity::class,
        ReservationEntity::class // ✅ Añadido
    ],
    version = 5, // ✅ Subida de versión
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryDao(): ItineraryItemDao
    abstract fun userDao(): UserDao
    abstract fun tripImageDao(): TripImageDao
    abstract fun reservationDao(): ReservationDao // ✅ Método nuevo
}
