package com.example.LowTravel.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.core.app.ActivityCompat
import java.util.Locale

fun getLanguagePreference(context: Context): String {
    val sharedPrefs = context.getSharedPreferences("LowTravel_preferences", Context.MODE_PRIVATE)
    return sharedPrefs.getString("user_language", "en") ?: "en"
}

fun applyPersistedLanguage(context: Context): Context {
    val languageCode = getLanguagePreference(context)
    return setAppLocale(context, languageCode)
}

fun setAppLocale(context: Context, languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val localeList = LocaleList(locale)
        config.setLocales(localeList)
    } else {
        config.locale = locale
    }

    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    return context
}

// Esta función actualiza el idioma y reinicia las actividades de manera controlada
fun updateLocaleAndRecreate(activity: Activity, languageCode: String) {
    // Primero guardamos el idioma en preferencias
    val sharedPrefs = activity.getSharedPreferences("LowTravel_preferences", Context.MODE_PRIVATE)
    sharedPrefs.edit().putString("user_language", languageCode).apply()

    // Actualizamos la configuración
    setAppLocale(activity, languageCode)

    // Recreamos la actividad de una manera más controlada
    val intent = activity.intent
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.finish()
    activity.startActivity(intent)
    ActivityCompat.recreate(activity)
}