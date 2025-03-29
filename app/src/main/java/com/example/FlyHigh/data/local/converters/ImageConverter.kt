package com.example.FlyHigh.data.local.converters

import androidx.room.TypeConverter
import com.example.FlyHigh.domain.model.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ImageListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromImageList(list: List<Image>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toImageList(json: String?): List<Image>? {
        if (json == null) return null
        val type: Type = object : TypeToken<List<Image>>() {}.type
        return gson.fromJson(json, type)
    }
}
