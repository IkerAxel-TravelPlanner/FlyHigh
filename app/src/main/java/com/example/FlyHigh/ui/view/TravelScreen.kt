package com.example.FlyHigh.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.data.local.entity.TripEntity
import com.example.FlyHigh.ui.viewmodel.TravelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelScreen(navController: NavController, viewModel: TravelViewModel) {
    // Obtener la lista de viajes en tiempo real
    val travels by viewModel.getAllTrips().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Viajes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createViaje") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Viaje")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (travels.isEmpty()) {
                Text("No hay viajes disponibles.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(travels) { travel ->
                        TravelItem(
                            travel = travel,
                            navController = navController,
                            onDeleteTravel = { viewModel.deleteTravel(travel.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TravelItem(travel: TripEntity, navController: NavController, onDeleteTravel: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("viaje/${travel.id}/itinerarios") },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = travel.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = travel.description, style = MaterialTheme.typography.bodyMedium)

            // Botón de eliminar viaje
            IconButton(onClick = { onDeleteTravel() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Viaje")
            }
        }
    }
}
