package com.example.navigation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AboutScreen1(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Acerca de Nosotros",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón "Información" con color azul consistente
        ElevatedButton(
            onClick = { navController.navigate("about/details") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Información", color = Color.White) // Texto en blanco
        }

        // Botón "Términos y Condiciones" con color azul consistente
        ElevatedButton(
            onClick = { navController.navigate("about/terms") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Términos y Condiciones", color = Color.White) // Texto en blanco
        }

        // Botón "Política de Privacidad" con color azul consistente
        ElevatedButton(
            onClick = { navController.navigate("about/privacy") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Política de Privacidad", color = Color.White) // Texto en blanco
        }
    }
}
