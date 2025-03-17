package com.example.navigation.modules

import android.health.connect.datatypes.ExerciseRoute
import java.util.Date

class ItineraryItem(
    var itemId: String,
    var title: String,
    var description: String,
    var startTime: Date,
    var endTime: Date,
    var location: ExerciseRoute.Location
)