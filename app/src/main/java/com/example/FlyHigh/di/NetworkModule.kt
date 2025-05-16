package com.example.FlyHigh.di

import com.example.FlyHigh.data.remote.api.HotelApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public final class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://13.39.162.212") // pon tu base url aquí
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHotelApiService(retrofit: Retrofit): HotelApiService {
        return retrofit.create(HotelApiService::class.java)
    }
}