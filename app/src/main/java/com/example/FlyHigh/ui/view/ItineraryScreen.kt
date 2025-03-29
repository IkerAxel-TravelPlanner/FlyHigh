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
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.ui.viewmodel.TravelViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(navController: NavController, viewModel: TravelViewModel, viajeId: String) {
    val viajeIdLong = viajeId.toLongOrNull() ?: -1L

    // Obtener el viaje en tiempo real
    val viaje by viewModel.getTripById(viajeIdLong).collectAsState(initial = null)

    // Obtener itinerarios en tiempo real
    val itinerarios by viewModel.getItinerariesByTripId(viajeIdLong).collectAsState(initial = emptyList())

    // Si el viaje no existe, regresar
    if (viaje == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Itinerarios de ${viaje?.title ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("viaje/$viajeId/createItinerary") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Itinerario")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (itinerarios.isEmpty()) {
                Text("No hay itinerarios disponibles.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(itinerarios) { itinerary ->
                        ItineraryItem(itinerary, navController, viajeId, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ItineraryItem(itinerary: ItineraryItemEntity, navController: NavController, viajeId: String, viewModel: TravelViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("viaje/$viajeId/itinerario/${itinerary.id}") },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itinerary.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)

            // Botón de eliminar itinerario
            IconButton(onClick = { viewModel.deleteItinerary(itinerary.id) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Itinerario")
            }
        }
    }
}
