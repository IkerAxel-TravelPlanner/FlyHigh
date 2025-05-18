package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.local.dao.TripImageDao
import com.example.FlyHigh.data.local.entity.TripImageEntity
import com.example.FlyHigh.domain.model.TripImage
import javax.inject.Inject

class TripImageRepository @Inject constructor(
    private val dao: TripImageDao
) : TripImageInterface {

    override suspend fun saveImage(tripId: Long, uri: String) {
        dao.insert(TripImageEntity(tripId = tripId, uri = uri))
    }

    override suspend fun getImages(tripId: Long): List<TripImage> {
        return dao.getImagesByTripId(tripId).map {
            TripImage(id = it.id, tripId = it.tripId, uri = it.uri)
        }
    }
    override suspend fun deleteImage(tripId: Long, uri: String) {
        dao.deleteImageByTripIdAndUri(tripId, uri)
    }

}
