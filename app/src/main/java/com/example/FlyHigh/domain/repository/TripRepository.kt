package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.local.dao.TripDao
import com.example.FlyHigh.data.local.entity.TripEntity
import com.example.FlyHigh.domain.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
    private val tripDao: TripDao
) : TripInterface {

    override suspend fun addTrip(
        userId: Long,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String,
        description: String,
        imageUrl: String?
    ): Long {
        val tripEntity = TripEntity(
            userId = userId,
            title = name,
            startDate = startDate,
            endDate = endDate,
            destination = destination,
            description = description,
            imageUrl = imageUrl
        )

        return tripDao.insertTrip(tripEntity)
    }

    override suspend fun editTrip(
        tripId: Long,
        userId: Long,
        name: String,
        startDate: Date,
        endDate: Date,
        destination: String,
        description: String,
        imageUrl: String?
    ) {
        // Primero verificamos que el viaje pertenece al usuario
        val tripEntity = tripDao.getTripByIdAndUserId(tripId, userId).firstOrNull()
            ?: throw IllegalArgumentException("Trip with ID $tripId not found for user $userId")

        val updatedTrip = tripEntity.copy(
            title = name,
            startDate = startDate,
            endDate = endDate,
            destination = destination,
            description = description,
            imageUrl = imageUrl ?: tripEntity.imageUrl
        )

        tripDao.updateTrip(updatedTrip)
    }

    override suspend fun deleteTrip(tripId: Long, userId: Long) {
        tripDao.deleteTripById(tripId, userId)
    }

    override fun getTripsForUser(userId: Long): Flow<List<Trip>> {
        return tripDao.getTripsByUserId(userId).asFlow().map { entities ->
            entities.map { it.toTrip() }
        }
    }

    override fun getTripById(tripId: Long, userId: Long): Flow<Trip?> {
        return tripDao.getTripByIdAndUserId(tripId, userId).map { it?.toTrip() }
    }

    // Extension function to convert TripEntity to Trip domain model
    private fun TripEntity.toTrip(): Trip {
        return Trip(
            map = null,  // Valor por defecto ya que no hay equivalente en TripEntity
            id = this.id.toString(),  // Convertir Long a String
            user = null,  // Valor por defecto ya que no hay equivalente directo en TripEntity
            destination = this.destination,
            itineraries = emptyList(),  // Valor por defecto
            startDate = this.startDate.toString(),
            endDate = this.endDate.toString(),
            images = emptyList(),  // Valor por defecto
            aiRecommendations = emptyList(),  // Valor por defecto
            imageURL = this.imageUrl ?: "https://example.com/default-trip-image.jpg"
        )
    }
}