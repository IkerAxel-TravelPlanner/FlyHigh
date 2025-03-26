package com.example.FlyHigh.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: Long,
    val passwordHash: String
)
