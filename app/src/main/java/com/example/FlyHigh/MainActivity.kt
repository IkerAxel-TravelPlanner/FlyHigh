package com.example.FlyHigh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.FlyHigh.ui.viewmodel.ThemeViewModel
import com.example.FlyHigh.ui.theme.NavigationTheme
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import com.example.FlyHigh.ui.viewmodel.TravelViewModelFactory
import com.example.FlyHigh.data.local.database.AppDatabase

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    private lateinit var travelViewModel: TravelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos correctamente
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()

        val factory = TravelViewModelFactory(db.tripDao(), db.itineraryDao())
        travelViewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        setContent {
            NavigationTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, travelViewModel = travelViewModel)
                }
            }
        }
    }
}
