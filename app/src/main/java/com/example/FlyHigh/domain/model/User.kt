package com.example.FlyHigh.domain.model

import java.util.Date

class User(
    var userId: String,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var dateOfBirth: Date,
    var passwordHash: String,
    //var trips: List<Trip>
)