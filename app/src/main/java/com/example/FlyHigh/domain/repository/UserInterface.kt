package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserInterface {
    suspend fun registerUser(
        username: String,
        email: String,
        birthDate: Date,
        address: String,
        country: String,
        phoneNumber: String,
        acceptEmailsOffers: Boolean,
        firebaseUid: String?
    ): Long

    suspend fun updateUser(user: User)

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserByUsername(username: String): User?

    suspend fun isUsernameExists(username: String): Boolean

    suspend fun getUserByFirebaseUid(firebaseUid: String): User?

    fun getUserById(userId: Long): Flow<User?>
}