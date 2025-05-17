package com.example.FlyHigh.utils

const val BASE_IMAGE_URL = "http://13.39.162.212"

fun String.asFullUrl(): String {
    return if (startsWith("http")) this else "$BASE_IMAGE_URL$this"
}