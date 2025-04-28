package com.example.FlyHigh.domain.repository

import com.example.FlyHigh.data.local.dao.UserDao
import com.example.FlyHigh.data.local.entity.UserEntity
import com.example.FlyHigh.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) : UserInterface {

    override suspend fun registerUser(
        username: String,
        email: String,
        birthDate: Date,
        address: String,
        country: String,
        phoneNumber: String,
        acceptEmailsOffers: Boolean,
        firebaseUid: String?
    ): Long {
        val userEntity = UserEntity(
            username = username,
            email = email,
            birthDate = birthDate,
            address = address,
            country = country,
            phoneNumber = phoneNumber,
            acceptEmailsOffers = acceptEmailsOffers,
            firebaseUid = firebaseUid
        )

        return userDao.insertUser(userEntity)
    }

    override suspend fun updateUser(user: User) {
        val userEntity = UserEntity(
            id = user.id,
            username = user.username,
            email = user.email,
            birthDate = user.birthDate,
            address = user.address,
            country = user.country,
            phoneNumber = user.phoneNumber,
            acceptEmailsOffers = user.acceptEmailsOffers,
            firebaseUid = user.firebaseUid
        )
        userDao.updateUser(userEntity)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toUser()
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)?.toUser()
    }

    override suspend fun isUsernameExists(username: String): Boolean {
        return userDao.isUsernameExists(username)
    }

    override suspend fun getUserByFirebaseUid(firebaseUid: String): User? {
        return userDao.getUserByFirebaseUid(firebaseUid)?.toUser()
    }

    override fun getUserById(userId: Long): Flow<User?> {
        return userDao.getUserById(userId).map { it?.toUser() }
    }

    // Extension function to convert UserEntity to User domain model
    private fun UserEntity.toUser(): User {
        return User(
            id = this.id,
            firebaseUid = this.firebaseUid,
            username = this.username,
            email = this.email,
            birthDate = this.birthDate,
            address = this.address,
            country = this.country,
            phoneNumber = this.phoneNumber,
            acceptEmailsOffers = this.acceptEmailsOffers
        )
    }
}