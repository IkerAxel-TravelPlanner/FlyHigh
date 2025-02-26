package com.example.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.navigation.ui.screens.Content1
import com.example.navigation.ui.screens.HomeScreen
import com.example.navigation.ui.screens.HomeScreenMenu
import com.example.navigation.ui.screens.LoginScreen
import com.example.navigation.ui.screens.ProfileScreen

//@Composable
//fun NavGraph(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") { LoginScreen(navController) }
//        composable("home") { HomeScreen(navController) }
//        composable("profile") { ProfileScreen(navController, null) }
//        composable("profile/{id}",
//                arguments = listOf(navArgument("id"){
//                            type = NavType.IntType
//                })
//        ) {
//            var userId = it.arguments?.getInt("id") ?: -1
//            ProfileScreen(navController, userId)
//        }
//    }
//}


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreenMenu(navController) { innerPadding ->
            Content1(navController, innerPadding)
        }}
        composable("profile") { ProfileScreen(navController, null) }
        composable("profile/{id}",
                arguments = listOf(navArgument("id"){
                            type = NavType.IntType
                })
        ) {
            var userId = it.arguments?.getInt("id") ?: -1
            ProfileScreen(navController, userId)
        }
        composable("profileMenu") { ProfileScreen(navController, null) }
    }
}
