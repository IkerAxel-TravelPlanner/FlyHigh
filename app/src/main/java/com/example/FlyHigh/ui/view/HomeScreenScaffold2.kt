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
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
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
                        val menuItems = listOf(
                            Triple("about", Icons.Filled.Info, "Acerca de"),
                            Triple("profile", Icons.Filled.Person, "Perfil"),
                            Triple("language_settings", Icons.Filled.Language, "Idioma"),
                            Triple("settings", Icons.Filled.Settings, "Configuración"),
                            Triple("version", Icons.Filled.Info, "Versión")
                        )

                        menuItems.forEach { (route, icon, label) ->
                            DropdownMenuItem(
                                leadingIcon = { Icon(icon, contentDescription = null) },
                                text = { Text(label) },
                                onClick = {
                                    showSettingsMenu = false
                                    navController.navigate(route)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex) { newIndex ->
                if (selectedIndex != newIndex) {
                    selectedIndex = newIndex
                    navController.navigate(
                        when (newIndex) {
                            0 -> "home"
                            1 -> "viajes"
                            else -> "explore"
                        }
                    )
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
            Spacer(modifier = Modifier.height(16.dp))
            TitleText("¡Organiza tu próximo viaje!")

            // Tarjeta: Buscar Hoteles
            FeatureCard(
                title = "Buscar Hoteles",
                description = "Encuentra el alojamiento perfecto para tu viaje.",
                imageRes = R.drawable.imagen_hotel,
                navController = navController,
                route = "hotel_search"
            )

            // ✅ NUEVA TARJETA: Mis Reservas
            FeatureCard(
                title = "Mis Reservas",
                description = "Consulta y gestiona tus reservas de hotel.",
                imageRes = R.drawable.imagen_reserva, // Usa cualquier imagen válida de tu proyecto
                navController = navController,
                route = "reservations"
            )

            Spacer(modifier = Modifier.height(16.dp))
            QuickAccessRow(navController)
            Spacer(modifier = Modifier.height(24.dp))

            FeatureCard("Descubre lugares increíbles", "Explora destinos únicos.", R.drawable.imagen1, navController, "explore")
            FeatureCard("Gestiona tus itinerarios", "Organiza tus actividades.", R.drawable.imagen7, navController, "viajes")
        }
    }
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.imagen_header),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
    )
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun QuickAccessRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuickAccessCard("Mis Viajes", Icons.Filled.List) { navController.navigate("viajes") }
        QuickAccessCard("Explorar", Icons.Filled.Explore) { navController.navigate("explore") }
        QuickAccessCard("Hoteles", Icons.Filled.Hotel) { navController.navigate("hotel_search") }
    }
}

@Composable
fun QuickAccessCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .height(110.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FeatureCard(title: String, description: String, imageRes: Int, navController: NavController, route: String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { navController.navigate(route) }
    ) {
        Column {
            Image(painter = painterResource(id = imageRes), contentDescription = title, modifier = Modifier.fillMaxWidth().height(150.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        listOf("Inicio" to Icons.Filled.Menu, "Viajes" to Icons.Filled.List, "Explorar" to Icons.Filled.Explore)
            .forEachIndexed { index, (label, icon) ->
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) },
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) }
                )
            }
    }
}
