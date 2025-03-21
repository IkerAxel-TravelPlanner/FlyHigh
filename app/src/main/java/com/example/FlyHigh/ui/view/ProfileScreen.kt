package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userId: Int?) {
    // Simulamos datos de usuario, en la realidad estos valores vendrían de una base de datos
    val username = "Usuario $userId"
    val email = "usuario$userId@example.com"
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
                text = stringResource(id = R.string.profile_name, username),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Correo electrónico
            Text(
                text = stringResource(id = R.string.profile_email, email),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Biografía
            Text(
                text = stringResource(id = R.string.profile_bio, bio),
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
        }
    }
}
