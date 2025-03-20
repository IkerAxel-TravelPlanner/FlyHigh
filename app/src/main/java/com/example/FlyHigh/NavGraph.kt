package com.example.FlyHigh

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.FlyHigh.ui.view.*
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel

@Composable
fun NavGraph(navController: NavHostController, itineraryViewModel: ItineraryViewModel) {
    NavHost(navController = navController, startDestination = "home") {

        composable("login") { LoginScreen2(navController) }
        composable("home") { HomeScreenScaffold2(navController) }
        composable("profile") { ProfileScreen(navController, null) }
        composable("profile/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val userId = it.arguments?.getInt("id") ?: -1
            ProfileScreen(navController, userId)
        }
        composable("profileMenu") { ProfileScreen(navController, null) }
        composable("about") { AboutScreen1(navController) }
        composable("about/details") { AboutScreen2(navController) }
        composable("about/terms") { TermsAndConditionsScreen(navController) }
        composable("about/privacy") { PrivacyPolicyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("version") { VersionScreen(navController) }
        composable("language_settings") { LanguageSettingsScreen(navController, context = LocalContext.current) }

        // 🔹 VIAJES
        composable("viajes") { TravelScreen(navController, itineraryViewModel) }
        composable("createViaje") { CreateTravelScreen(navController, itineraryViewModel) }
        composable("viaje/{viajeId}", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            TravelDetailScreen(navController, itineraryViewModel, viajeId)
        }

        // 🔹 LISTADO DE ITINERARIOS DENTRO DE UN VIAJE
        composable("viaje/{viajeId}/itinerarios", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            ItineraryListScreen(navController, itineraryViewModel, viajeId)
        }

        // 🔹 CREAR UN ITINERARIO EN UN VIAJE
        composable("viaje/{viajeId}/createItinerario", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            CreateItineraryScreen(navController, itineraryViewModel, viajeId)
        }

        // 🔹 EDITAR UN ITINERARIO
        composable("viaje/{viajeId}/itinerario/{itineraryId}", arguments = listOf(
            navArgument("viajeId") { type = NavType.StringType },
            navArgument("itineraryId") { type = NavType.StringType }
        )) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: return@composable
            EditItineraryScreen(navController, itineraryViewModel, viajeId, itineraryId)
        }
    }
}
