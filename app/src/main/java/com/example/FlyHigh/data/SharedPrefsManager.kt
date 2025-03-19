package com.example.LowTravel.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsManager @Inject constructor(
    private val preferences: SharedPreferences
) {
    var userLanguage: String?
        get() = preferences.getString("user_language", "en")
        set(value) {
            preferences.edit().putString("user_language", value).apply()
        }

    var darkTheme: Boolean
        get() = preferences.getBoolean("dark_theme", false)
        set(value) = preferences.edit().putBoolean("dark_theme", value).apply()
}