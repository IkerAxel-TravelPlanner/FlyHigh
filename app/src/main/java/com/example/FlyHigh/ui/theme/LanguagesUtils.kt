package com.example.FlyHigh.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(locale)
    } else {
        config.locale = locale
    }

    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
