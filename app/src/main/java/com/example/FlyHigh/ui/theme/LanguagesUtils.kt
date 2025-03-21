import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

fun setLocale(context: Context, languageCode: String) {
    // Establece la nueva configuración regional (locale)
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(locale)
    } else {
        config.locale = locale
    }

    // Actualiza la configuración de recursos con la nueva configuración regional
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Si la actividad está en primer plano, reiniciamos la actividad para que los cambios tomen efecto
    if (context is Activity) {
        reloadActivity(context)
    }
}

fun reloadActivity(context: Activity) {
    // Reinicia la actividad para aplicar el cambio de idioma
    val intent = context.intent
    context.finish()
    context.startActivity(intent)
    context.overridePendingTransition(0, 0) // Opcional: Para evitar la animación al reiniciar la actividad
}
