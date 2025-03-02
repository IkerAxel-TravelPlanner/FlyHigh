package com.example.navigation.modules

import java.util.Date

class User(
    var userId: String,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var dateOfBirth: Date,
    var passwordHash: String,
    var trips: List<Trip>
) {
    fun register(email: String, password: String) {}
    fun updateProfile() {}
    fun deleteAccount() {}
}