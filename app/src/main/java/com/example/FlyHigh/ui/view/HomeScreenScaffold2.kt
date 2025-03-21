package com.example.FlyHigh.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold2(navController: NavController) {
    var showSettingsMenu by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showSettingsMenu = !showSettingsMenu }) {
                        Icon(Icons.Outlined.Settings, contentDescription = stringResource(id = R.string.settings))
                    }
                    DropdownMenu(
                        expanded = showSettingsMenu,
                        onDismissRequest = { showSettingsMenu = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                            text = { Text(stringResource(id = R.string.about)) },
                            onClick = {
                                showSettingsMenu = false
                                navController.navigate("about")
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                            text = { Text(stringResource(id = R.string.profile)) },
                            onClick = {
                                showSettingsMenu = false
                                navController.navigate("profile")
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex) { newIndex ->
                selectedIndex = newIndex
                when (newIndex) {
                    0 -> navController.navigate("home")
                    1 -> navController.navigate("viajes")
                    2 -> navController.navigate("explore")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Encabezado con imagen
            Image(
                painter = painterResource(id = R.drawable.imagen1), // Cambia por una imagen real
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título principal
            Text(
                text = "¡Organiza tu próximo viaje!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de acceso rápido
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickAccessCard(
                    title = "Mis Viajes",
                    icon = Icons.Filled.List,
                    onClick = { navController.navigate("viajes") }
                )
                QuickAccessCard(
                    title = "Explorar",
                    icon = Icons.Filled.Explore,
                    onClick = { navController.navigate("explore") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de opciones destacadas
            FeatureCard(
                title = "Descubre lugares increíbles",
                description = "Explora destinos únicos y encuentra tu próximo destino soñado.",
                imageRes = R.drawable.imagen1, // Imagen de exploración
                onClick = { navController.navigate("explore") }
            )

            FeatureCard(
                title = "Gestiona tus itinerarios",
                description = "Organiza tus actividades y haz que cada viaje sea perfecto.",
                imageRes = R.drawable.imagen1, // Imagen de itinerarios
                onClick = { navController.navigate("viajes") }
            )
        }
    }
}

// 📌 Componente para Tarjetas de Acceso Rápido
@Composable
fun QuickAccessCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// 📌 Componente para Tarjetas Destacadas con Imagen
@Composable
fun FeatureCard(title: String, description: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

// 📌 Barra de navegación inferior
@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Menu, contentDescription = "Menú") },
            label = { Text("Inicio") },
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Viajes") },
            label = { Text("Viajes") },
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Explore, contentDescription = "Explorar") },
            label = { Text("Explorar") },
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) }
        )
    }
}
