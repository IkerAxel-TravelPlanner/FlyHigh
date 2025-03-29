package com.example.FlyHigh.data.local.converters

import androidx.room.TypeConverter
import com.example.FlyHigh.domain.model.Map
import com.google.gson.Gson

class MapConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map?): String? {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(json: String?): Map? {
        return if (json == null) null else gson.fromJson(json, Map::class.java)
    }
}
