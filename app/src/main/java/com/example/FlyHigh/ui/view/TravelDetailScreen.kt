package com.example.FlyHigh.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel
import com.example.FlyHigh.ui.viewmodel.Travel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDetailScreen(navController: NavController, viewModel: ItineraryViewModel, viajeId: String) {
    val viaje = remember { viewModel.travels.find { it.id == viajeId } }

    if (viaje == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(viaje.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("viaje/${viaje.id}/createItinerario") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir itinerario")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text(text = viaje.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Itinerarios:", style = MaterialTheme.typography.headlineSmall)

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viaje.itineraries) { itinerary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("viaje/${viaje.id}/itinerario/${itinerary.id}") },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = itinerary.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
