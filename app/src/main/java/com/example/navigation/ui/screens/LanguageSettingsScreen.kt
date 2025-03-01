package com.example.navigation.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.navigation.util.setLocale  // Importa el método setLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(navController: NavController, context: Context) {
    var selectedLanguage by remember { mutableStateOf("") }

    // Usamos el color morado que ya se usa en la app
    val primaryColor = Color(0xFF6200EE)  // El color morado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Idioma") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = primaryColor)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),  // Espaciado entre los botones
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Selecciona un idioma",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Botón para inglés
                ElevatedButton(
                    onClick = {
                        setLocale(context, "en")  // Cambiar a inglés
                        navController.popBackStack()  // Cerrar la pantalla de configuración
                        navController.navigate("home")  // Recargar la pantalla de inicio para aplicar el cambio de idioma
                    },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Inglés", color = Color.White)
                }

                // Botón para español
                ElevatedButton(
                    onClick = {
                        setLocale(context, "es")  // Cambiar a español
                        navController.popBackStack()  // Cerrar la pantalla de configuración
                        navController.navigate("home")  // Recargar la pantalla de inicio para aplicar el cambio de idioma
                    },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Español", color = Color.White)
                }

                // Botón para portugués
                ElevatedButton(
                    onClick = {
                        setLocale(context, "pt")  // Cambiar a portugués
                        navController.popBackStack()  // Cerrar la pantalla de configuración
                        navController.navigate("home")  // Recargar la pantalla de inicio para aplicar el cambio de idioma
                    },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Portugués", color = Color.White)
                }
            }
        }
    )
}
