package com.example.FlyHigh.di

import com.example.FlyHigh.domain.repository.HotelInterface
import com.example.FlyHigh.domain.repository.HotelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHotelRepository(
        hotelRepository: HotelRepository
    ): HotelInterface
}
