package com.example.FlyHigh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.FlyHigh.ui.viewmodel.ThemeViewModel
import com.example.FlyHigh.ui.theme.NavigationTheme
import com.example.FlyHigh.ui.viewmodel.TravelViewModel

class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels() // ViewModel para gestionar el tema
    private val travelViewModel: TravelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavigationTheme(darkTheme = themeViewModel.isDarkTheme.value) {  // Pasar el valor del tema
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, travelViewModel = travelViewModel)
                }
            }
        }
    }
}
