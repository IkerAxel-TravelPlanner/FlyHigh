package com.example.FlyHigh.domain.repository
import android.health.connect.datatypes.ExerciseRoute
import com.example.FlyHigh.domain.model.ItineraryItem
import java.util.Date

interface ItineraryItemInterface {
        fun addActivity(itemId: String,title: String,description: String,startTime: Date,endTime: Date,location: ExerciseRoute.Location):ItineraryItem
        fun updateActivity(itemId: String,title: String,description: String,startTime: Date,endTime: Date,location: ExerciseRoute.Location):ItineraryItem
        fun deleteActivity(itemId: String)
}