package com.example.FlyHigh.data.local.converters

import androidx.room.TypeConverter
import com.example.FlyHigh.domain.model.ItineraryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ItineraryItemListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromItineraryItemList(list: List<ItineraryItem>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toItineraryItemList(json: String?): List<ItineraryItem>? {
        if (json == null) return null
        val listType: Type = object : TypeToken<List<ItineraryItem>>() {}.type
        return gson.fromJson(json, listType)
    }
}
