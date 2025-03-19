package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.FlyHigh.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón "Acerca de" que abre el AboutScreen1
        ElevatedButton(
            onClick = { navController.navigate("about") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.about), color = Color.White) // Color texto blanco
        }

        // Botón para "Versión" que abre la pantalla de VersionScreen
        ElevatedButton(
            onClick = { navController.navigate("version") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.version), color = Color.White) // Color texto blanco
        }

        // Botón para cambiar idioma
        ElevatedButton(
            onClick = { navController.navigate("language_settings") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.change_language), color = Color.White) // Color texto blanco
        }

        // Botón para notificaciones (aún no implementado)
        ElevatedButton(
            onClick = { /* TODO: Implementar ajustes de notificaciones */ },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.notifications), color = Color.White) // Color texto blanco
        }

        // Botón para ajustes de seguridad (aún no implementado)
        ElevatedButton(
            onClick = { /* TODO: Implementar ajustes de seguridad */ },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.security_privacy), color = Color.White) // Color texto blanco
        }

        // Botón para ajustes avanzados (aún no implementado)
        ElevatedButton(
            onClick = { /* TODO: Implementar ajustes avanzados */ },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
        ) {
            Text(text = stringResource(id = R.string.advanced_settings), color = Color.White) // Color texto blanco
        }
    }
}
