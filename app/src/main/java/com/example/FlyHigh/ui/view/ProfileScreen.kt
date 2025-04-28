// ProfileScreen.kt - Modified version with logout functionality
package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userId: Int?) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Show confirmation dialog state
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Simulamos datos de usuario o usamos datos reales si están disponibles
    val username = currentUser?.displayName ?: "Usuario $userId"
    val email = currentUser?.email ?: "usuario$userId@example.com"
    val bio = "¡Hola! Soy un usuario de la app. Me encanta la tecnología y aprender cosas nuevas."

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(id = R.string.profile_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = padding.calculateTopPadding()), // Padding superior para evitar que el contenido se solape con la barra
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de perfil (simulada como un círculo)
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large,
                color = Color.Gray
            ) {
                // Aquí se puede agregar una imagen de perfil real
            }

            // Nombre de usuario
            Text(
                text = username,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Correo electrónico
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Biografía
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón para editar el perfil
            ElevatedButton(
                onClick = { /* Aquí iría la acción para editar el perfil */ },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.edit_profile), color = Color.White)
            }

            // Botón para volver a la pantalla de inicio
            ElevatedButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.back_to_home), color = Color.White)
            }

            // Botón de cerrar sesión
            ElevatedButton(
                onClick = { showLogoutDialog = true },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cerrar sesión", color = Color.White)
                }
            }
        }
    }

    // Dialog de confirmación para cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                        showLogoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}