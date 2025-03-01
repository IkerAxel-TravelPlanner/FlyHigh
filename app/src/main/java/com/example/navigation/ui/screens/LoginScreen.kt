package com.example.navigation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón de login con color consistente
        ElevatedButton(
            onClick = {
                // Navegar a la pantalla Home y eliminar Login de la pila de navegación
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Login", color = Color.White)
        }
    }
}
