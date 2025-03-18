package com.example.navigation.ui.view

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
fun AboutScreen2(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Acerca de Nosotros",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Esta aplicación ha sido creada con el objetivo de facilitar la navegación y mejorar la experiencia de usuario. Nuestro equipo está comprometido en proporcionar una plataforma confiable y segura.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Gracias por confiar en nosotros.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón "Volver" en azul consistente
        ElevatedButton(
            onClick = { navController.popBackStack() },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Volver", color = Color.White) // Color blanco para el texto
        }
    }
}
