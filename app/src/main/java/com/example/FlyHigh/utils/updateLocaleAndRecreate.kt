package com.example.LowTravel.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

// Actualiza la configuración del idioma y reinicia la actividad
fun updateLocaleAndRecreate(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(locale)
    } else {
        config.locale = locale
    }

    // Verificar si el contexto es una instancia de Activity antes de llamar a recreate
    if (context is Activity) {
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.recreate()  // Recarga la actividad con la nueva configuración
    }
}
