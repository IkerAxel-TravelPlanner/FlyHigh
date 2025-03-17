package com.example.navigation.repository

import com.example.navigation.modules.User
import java.util.Date

interface UserInterface {
    fun register(userId: String,name: String,email: String,phoneNumber: String,dateOfBirth: Date,passwordHash: String) {}
    fun updateProfile(userId: String,name: String,email: String,phoneNumber: String,dateOfBirth: Date,passwordHash: String) {}
    fun deleteAccount(userId: String) {}
}

