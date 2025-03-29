package com.example.FlyHigh.data.local.converters

import androidx.room.TypeConverter
import com.example.FlyHigh.domain.model.AIRecommendations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AIRecommendationsListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromAIRecommendationsList(list: List<AIRecommendations>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toAIRecommendationsList(json: String?): List<AIRecommendations>? {
        if (json == null) return null
        val type: Type = object : TypeToken<List<AIRecommendations>>() {}.type
        return gson.fromJson(json, type)
    }
}
