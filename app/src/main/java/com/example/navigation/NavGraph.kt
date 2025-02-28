package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.navigation.ui.screens.*

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

        // Sección de Configuración (Settings)
        composable("settings") { SettingsScreen(navController) }

        // Pantalla de "About" donde se muestra la información de "About Us"
        composable("about/us") { AboutUsScreen(navController) }

        // Pantalla de Acerca de Nosotros (Información adicional)
        composable("settings/about") { AboutScreen2(navController) }

        // Pantalla de Términos y Condiciones
        composable("settings/terms") { TermsAndConditionsScreen(navController) }

        // Pantalla de Política de Privacidad
        composable("settings/privacy") { PrivacyPolicyScreen(navController) }
    }
}
