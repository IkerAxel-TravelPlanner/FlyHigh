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
import com.example.FlyHigh.ui.viewmodel.TravelViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryListScreen(navController: NavController, viewModel: TravelViewModel, viajeId: String) {
    val viaje by viewModel.getTripById(viajeId.toLongOrNull() ?: -1).collectAsState(initial = null)
    val itinerarios by viewModel.getItinerariesByTripId(viajeId.toLongOrNull() ?: -1).collectAsState(initial = emptyList())

    if (viaje == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Itinerarios de ${viaje!!.title}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("viaje/${viaje!!.id}/createItinerario") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir itinerario")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (itinerarios.isEmpty()) {
                Text(
                    text = "No hay itinerarios. Agrega uno con el botón +.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(itinerarios) { itinerary ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { navController.navigate("viaje/${viaje!!.id}/itinerario/${itinerary.id}") },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = itinerary.title, style = MaterialTheme.typography.bodyLarge)
                                Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)

                                // Botón de eliminar itinerario
                                IconButton(onClick = { viewModel.deleteItinerary(itinerary.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Itinerario")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
