package com.example.FlyHigh.domain.model

class Preferences(
    var userId: String,
    var notificationsEnabled: Boolean,
    var preferredLanguage: String,
    var theme: String,
    var currency: String,
    var darkMode: Boolean
) {
    fun updatePreferences() {}
}