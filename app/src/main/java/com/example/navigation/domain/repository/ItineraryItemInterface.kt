package com.example.navigation.domain.repository
import android.health.connect.datatypes.ExerciseRoute
import com.example.navigation.modules.ItineraryItem
import java.util.Date

interface ItineraryItemInterface {
        fun addActivity(itemId: String,title: String,description: String,startTime: Date,endTime: Date,location: ExerciseRoute.Location):ItineraryItem
        fun updateActivity(itemId: String,title: String,description: String,startTime: Date,endTime: Date,location: ExerciseRoute.Location):ItineraryItem
        fun deleteActivity(itemId: String)
}