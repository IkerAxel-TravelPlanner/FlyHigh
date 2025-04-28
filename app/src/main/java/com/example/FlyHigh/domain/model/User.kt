package com.example.FlyHigh.domain.model

import java.util.Date

data class User(
    val id: Long = 0,
    val firebaseUid: String? = null,
    val username: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val address: String = "",
    val country: String = "",
    val phoneNumber: String = "",
    val acceptEmailsOffers: Boolean = false
)