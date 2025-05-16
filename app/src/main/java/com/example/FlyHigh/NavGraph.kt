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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController, travelViewModel: TravelViewModel? = null) {
    val userLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (userLoggedIn) "home" else "login"
    val viewModel = travelViewModel ?: androidx.hilt.navigation.compose.hiltViewModel<TravelViewModel>()

    NavHost(navController = navController, startDestination = startDestination) {

        // 🔐 Auth
        composable("login") { LoginScreen2(navController) }
        composable("register") { RegisterScreen(navController) }
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

        // 🏠 Inicio
        composable("home") { HomeScreenScaffold2(navController) }

        // 👤 Perfil
        composable("profile") {
            ProfileScreen(navController, null)
        }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId")
            ProfileScreen(navController, userId)
        }
        composable(
            "editProfile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId")
            EditProfileScreen(navController, userId)
        }

        // ⚙️ Ajustes y legales
        composable("about") { AboutScreen1(navController) }
        composable("about/details") { AboutScreen2(navController) }
        composable("about/terms") { TermsAndConditionsScreen(navController) }
        composable("about/privacy") { PrivacyPolicyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("version") { VersionScreen(navController) }
        composable("language_settings") { LanguageSettingsScreen(navController, LocalContext.current) }

        // 🔔 Notificaciones y seguridad
        composable("notifications") { NotificationsScreen(navController) }
        composable("security_and_privacy") { SecurityAndPrivacyScreen(navController) }
        composable("advanced_settings") { AdvancedSettingsScreen(navController) }

        // ✈️ VIAJES
        composable("viajes") { TravelScreen(navController, viewModel) }
        composable("createViaje") { CreateTravelScreen(navController, viewModel) }

        // ✅ TravelDetailScreen con timestamp (para forzar recarga)
        composable(
            route = "viaje/{viajeId}?ts={ts}",
            arguments = listOf(
                navArgument("viajeId") { type = NavType.StringType },
                navArgument("ts") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            TravelDetailScreen(navController, viewModel, viajeId)
        }

        composable("editViaje/{viajeId}",
            arguments = listOf(navArgument("viajeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            EditTravelScreen(navController, viewModel, viajeId)
        }

        // 🏨 HOTELES
        composable("hotel_search") {
            HotelSearchScreen(navController)
        }

        composable(
            route = "hotel_detail/{hotelId}?startDate={startDate}&endDate={endDate}",
            arguments = listOf(
                navArgument("hotelId") { type = NavType.StringType },
                navArgument("startDate") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("endDate") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val hotelId = backStackEntry.arguments?.getString("hotelId") ?: return@composable
            val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
            val endDate = backStackEntry.arguments?.getString("endDate") ?: ""
            HotelDetailScreen(navController, hotelId, startDate, endDate)
        }

        // 🗓️ ITINERARIOS
        composable("viaje/{viajeId}/itinerarios",
            arguments = listOf(navArgument("viajeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: return@composable
            ItineraryScreen(navController, viewModel, viajeId)
        }

        composable("viaje/{viajeId}/createItinerario",
            arguments = listOf(navArgument("viajeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("viajeId")?.toLongOrNull() ?: -1L
            CreateItineraryScreen(navController, viewModel, tripId)
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
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }

        composable(
            route = "viaje/{viajeId}/itinerario/{itineraryId}/detail",
            arguments = listOf(
                navArgument("viajeId") { type = NavType.StringType },
                navArgument("itineraryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getString("viajeId") ?: ""
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: ""
            ItineraryDetailScreen(navController, viewModel, viajeId, itineraryId)
        }

        // 🔍 EXPLORAR
        composable("explore") { ExploreScreen(navController) }
        composable("explore/details") { ExploreDetailsScreen(navController) }
    }
}