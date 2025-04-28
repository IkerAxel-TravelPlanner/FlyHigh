package com.example.FlyHigh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.FlyHigh.ui.view.*
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import com.example.FlyHigh.ui.view.CreateItineraryScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController, travelViewModel: TravelViewModel? = null) {
    // Verificar si hay un usuario autenticado con Firebase
    val userLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (userLoggedIn) "home" else "login"

    // Si no se proporciona un viewModel, se puede manejar dentro de cada composable con hiltViewModel()
    val viewModel = travelViewModel ?: androidx.hilt.navigation.compose.hiltViewModel<TravelViewModel>()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") { LoginScreen2(navController) }
        composable("register") { RegisterScreen(navController) }

        // Nueva ruta para la pantalla de recuperación de contraseña
        composable(
            route = "forgot_password?email={email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordScreen(navController, email)
        }

        composable("home") { HomeScreenScaffold2(navController) }
        composable("profile") {
            ProfileScreen(navController, null)
        }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId")
            ProfileScreen(navController, userId)
        }

        // Ruta para editar perfil (AÑADIDA)
        composable("editProfile") {
            EditProfileScreen(navController)
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
        composable("viajes") { TravelScreen(navController, viewModel) }
        composable("createViaje") { CreateTravelScreen(navController, viewModel) }
        composable("viaje/{viajeId}", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                TravelDetailScreen(navController, viewModel, viajeId)
            }
        }
        composable("editViaje/{viajeId}", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                EditTravelScreen(navController, viewModel, viajeId)
            }
        }

        // 📌 ITINERARIOS
        composable("viaje/{viajeId}/itinerarios", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.let { viajeId ->
                ItineraryScreen(navController, viewModel, viajeId)
            }
        }
        composable("viaje/{viajeId}/createItinerario", arguments = listOf(navArgument("viajeId") { type = NavType.StringType })) { backStackEntry ->
            backStackEntry.arguments?.getString("viajeId")?.toLongOrNull()?.let { tripId ->
                CreateItineraryScreen(navController, viewModel, tripId)
            }
        }

        composable(
            route = "viaje/{viajeId}/itinerario/{itineraryId}",
            arguments = listOf(
                navArgument("viajeId") { type = NavType.StringType },
                navArgument("itineraryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId")
            val itineraryId = backStackEntry.arguments?.getString("itineraryId")

            if (viajeId != null && itineraryId != null) {
                EditItineraryScreen(
                    navController = navController,
                    viewModel = viewModel,
                    viajeId = viajeId.toLongOrNull() ?: -1L,
                    itineraryId = itineraryId.toLongOrNull() ?: -1L
                )
            } else {
                // Handle invalid arguments
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        // Route for viewing itinerary details
        composable(
            route = "viaje/{viajeId}/itinerario/{itineraryId}/detail",
            arguments = listOf(
                navArgument("viajeId") { type = NavType.StringType },
                navArgument("itineraryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: ""
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: ""

            ItineraryDetailScreen(
                navController = navController,
                viewModel = viewModel,
                viajeId = viajeId,
                itineraryId = itineraryId
            )
        }

        // 📌 EXPLORAR
        composable("explore") { ExploreScreen(navController) }

        composable("explore/details") {
            ExploreDetailsScreen(navController)
        }
    }
}