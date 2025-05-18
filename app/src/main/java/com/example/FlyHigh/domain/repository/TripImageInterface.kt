package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.TripImage

interface TripImageInterface {
    suspend fun saveImage(tripId: Long, uri: String)
    suspend fun getImages(tripId: Long): List<TripImage>
    suspend fun deleteImage(tripId: Long, uri: String)

}
