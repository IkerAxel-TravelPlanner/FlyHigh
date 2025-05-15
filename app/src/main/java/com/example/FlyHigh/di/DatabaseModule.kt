package com.example.FlyHigh.di

import android.content.Context
import androidx.room.Room
import com.example.FlyHigh.data.local.dao.ItineraryItemDao
import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.dao.TripImageDao
import com.example.FlyHigh.data.local.dao.UserDao
import com.example.FlyHigh.data.local.database.AppDatabase
import com.example.FlyHigh.data.local.database.DatabaseMigration
import com.example.FlyHigh.domain.repository.TripImageInterface
import com.example.FlyHigh.domain.repository.TripImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flyhigh_database"
        )
            .addMigrations(DatabaseMigration.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideTripDao(appDatabase: AppDatabase): TripDao {
        return appDatabase.tripDao()
    }

    @Provides
    @Singleton
    fun provideItineraryItemDao(appDatabase: AppDatabase): ItineraryItemDao {
        return appDatabase.itineraryDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideTripImageDao(appDatabase: AppDatabase): TripImageDao {
        return appDatabase.tripImageDao()
    }

    @Provides
    @Singleton
    fun provideTripImageRepository(dao: TripImageDao): TripImageInterface {
        return TripImageRepository(dao)
    }


}