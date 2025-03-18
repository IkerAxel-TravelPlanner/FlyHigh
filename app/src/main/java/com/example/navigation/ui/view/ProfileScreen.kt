package com.example.navigation.ui.view

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
fun ProfileScreen(navController: NavController, userId: Int?) {
    // Simulamos datos de usuario, en la realidad estos valores vendrían de una base de datos
    val username = "Usuario $userId"
    val email = "usuario$userId@example.com"
    val bio = "¡Hola! Soy un usuario de la app. Me encanta la tecnología y aprender cosas nuevas."

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(
            text = "Perfil de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar nombre de usuario
        Text(
            text = "Nombre: $username",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mostrar correo electrónico
        Text(
            text = "Correo: $email",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mostrar biografía
        Text(
            text = "Biografía: $bio",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón para editar el perfil (aún sin implementar la funcionalidad de edición)
        ElevatedButton(
            onClick = { /* Aquí iría la acción para editar el perfil */ },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Editar Perfil", color = Color.White)
        }

        // Botón para volver a la pantalla de inicio
        ElevatedButton(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color azul
        ) {
            Text(text = "Volver al Inicio", color = Color.White)
        }
    }
}
