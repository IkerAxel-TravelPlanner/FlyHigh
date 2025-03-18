package com.example.navigation.domain.model

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