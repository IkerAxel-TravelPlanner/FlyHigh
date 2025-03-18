package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.navigation.ui.view.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de Login
        composable("login") { LoginScreen2(navController) }

        // Pantalla de Home
        composable("home") { HomeScreenScaffold2(navController) }

        // Pantalla de Perfil
        composable("profile") { ProfileScreen(navController, null) }

        composable("profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            val userId = it.arguments?.getInt("id") ?: -1
            ProfileScreen(navController, userId)
        }

        composable("profileMenu") { ProfileScreen(navController, null) }

        // Sección de "Acerca de" (About Us)
        composable("about") { AboutScreen1(navController) }
        composable("about/details") { AboutScreen2(navController) }
        composable("about/terms") { TermsAndConditionsScreen(navController) }
        composable("about/privacy") { PrivacyPolicyScreen(navController) }

        // Pantalla de Configuración (Settings)
        composable("settings") { SettingsScreen(navController) }

        // Pantalla de Versión
        composable("version") { VersionScreen(navController) }

        // Pantalla de Configuración de Idioma
        composable("language_settings") { LanguageSettingsScreen(navController, context = LocalContext.current) }
    }
}
