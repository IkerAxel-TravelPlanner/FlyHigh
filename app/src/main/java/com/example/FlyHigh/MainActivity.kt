package com.example.FlyHigh

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.FlyHigh.ui.theme.NavigationTheme
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import com.example.FlyHigh.ui.viewmodel.ThemeViewModel
import com.example.LowTravel.utils.applyPersistedLanguage

class MainActivity : ComponentActivity() {

    private val travelViewModel: TravelViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels() // Asegúrate de obtener el ThemeViewModel

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { applyPersistedLanguage(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Asegúrate de pasar el valor booleano de isDarkTheme aquí
            NavigationTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, travelViewModel = travelViewModel)
                }
            }
        }
    }
}
