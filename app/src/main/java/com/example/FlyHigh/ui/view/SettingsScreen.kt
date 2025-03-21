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
import androidx.navigation.NavHostController
import com.example.FlyHigh.R
import com.example.FlyHigh.ui.viewmodel.ThemeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, themeViewModel: ThemeViewModel = viewModel()) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) },
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
                .padding(24.dp)
                .padding(top = padding.calculateTopPadding()), // Padding superior para evitar superposición con la barra
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botón "Acerca de"
            SettingButton(
                text = stringResource(id = R.string.about),
                onClick = { navController.navigate("about") }
            )

            // Botón "Versión"
            SettingButton(
                text = stringResource(id = R.string.version),
                onClick = { navController.navigate("version") }
            )

            // Botón "Cambiar Idioma"
            SettingButton(
                text = stringResource(id = R.string.change_language),
                onClick = { navController.navigate("language_settings") }
            )

            // Botón "Notificaciones"
            SettingButton(
                text = stringResource(id = R.string.notifications),
                onClick = { navController.navigate("notifications") }
            )

            // Botón "Seguridad y Privacidad"
            SettingButton(
                text = stringResource(id = R.string.security_privacy),
                onClick = { navController.navigate("security_and_privacy") }
            )

            // Botón "Ajustes Avanzados"
            SettingButton(
                text = stringResource(id = R.string.advanced_settings),
                onClick = { navController.navigate("advanced_settings") }
            )

            // Opción para cambiar tema
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.change_theme))

            Switch(
                checked = themeViewModel.isDarkTheme.value, // El estado del tema se obtiene desde el ViewModel
                onCheckedChange = { themeViewModel.toggleTheme() } // Al cambiar, se alterna el valor
            )
        }
    }
}

@Composable
fun SettingButton(text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color morado
    ) {
        Text(text = text, color = Color.White) // Texto blanco
    }
}
