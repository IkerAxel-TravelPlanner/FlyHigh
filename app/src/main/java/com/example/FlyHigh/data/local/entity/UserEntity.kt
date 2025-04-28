package com.example.FlyHigh.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firebaseUid: String? = null,
    val username: String,
    val email: String,
    val birthDate: Date,
    val address: String,
    val country: String,
    val phoneNumber: String,
    val acceptEmailsOffers: Boolean = false
)