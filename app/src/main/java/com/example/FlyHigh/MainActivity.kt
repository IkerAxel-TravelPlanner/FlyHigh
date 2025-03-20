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
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel
import com.example.LowTravel.utils.applyPersistedLanguage

class MainActivity : ComponentActivity() {

    private val itineraryViewModel: ItineraryViewModel by viewModels()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { applyPersistedLanguage(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavigationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, itineraryViewModel = itineraryViewModel)
                }
            }
        }
    }
}
