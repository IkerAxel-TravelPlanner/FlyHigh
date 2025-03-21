package com.example.FlyHigh

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.FlyHigh.ui.view.*
import com.example.FlyHigh.ui.viewmodel.TravelViewModel

@Composable
fun NavGraph(navController: NavHostController, travelViewModel: TravelViewModel) {

    val userLoggedIn = false // Sustituye por lógica real
    val startDestination = if (userLoggedIn) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") { LoginScreen2(navController) }
        composable("home") { HomeScreenScaffold2(navController) }
        composable("profile") { ProfileScreen(navController, null) }
        composable("profile/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val userId = it.arguments?.getInt("id") ?: -1
            ProfileScreen(navController, userId)
        }

        // 📌 Configuración y Acerca de
        composable("about") { AboutScreen1(navController) }
        composable("about/details") { AboutScreen2(navController) }
        composable("about/terms") { TermsAndConditionsScreen(navController) }
        composable("about/privacy") { PrivacyPolicyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("version") { VersionScreen(navController) }
        composable("language_settings") { LanguageSettingsScreen(navController, LocalContext.current) }

        // 📌 NUEVAS PANTALLAS
        composable("notifications") { NotificationsScreen(navController) }
        composable("security_and_privacy") { SecurityAndPrivacyScreen(navController) }
        composable("advanced_settings") { AdvancedSettingsScreen(navController) }

        // 📌 VIAJES
        composable("viajes") { TravelScreen(navController, travelViewModel) }
        composable("createViaje") { CreateTravelScreen(navController, travelViewModel) }
        composable("viaje/{viajeId}", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                TravelDetailScreen(navController, travelViewModel, viajeId)
            }
        }
        composable("editViaje/{viajeId}", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                EditTravelScreen(navController, travelViewModel, viajeId)
            }
        }

        // 📌 ITINERARIOS
        composable("viaje/{viajeId}/itinerarios", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                ItineraryListScreen(navController, travelViewModel, viajeId)
            }
        }
        composable("viaje/{viajeId}/createItinerario", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                CreateItineraryScreen(navController, travelViewModel, viajeId)
            }
        }
        composable("viaje/{viajeId}/itinerario/{itineraryId}", arguments = listOf(
            navArgument("viajeId") { type = NavType.StringType },
            navArgument("itineraryId") { type = NavType.StringType }
        )) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: return@composable
            EditItineraryScreen(navController, travelViewModel, viajeId, itineraryId)
        }
        composable("itinerary/{itineraryId}", arguments = listOf(navArgument("itineraryId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("itineraryId")?.let { itineraryId ->
                ItineraryScreen(navController, travelViewModel, itineraryId)
            }
        }

        // 📌 EXPLORAR
        composable("explore") { ExploreScreen(navController) }
    }
}
