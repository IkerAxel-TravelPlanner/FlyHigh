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
import com.example.FlyHigh.ui.view.ItineraryCard



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDetailScreen(navController: NavController, viewModel: TravelViewModel, viajeId: String) {
    val viajeIdLong = viajeId.toLongOrNull() ?: -1L

    // Obtener el viaje en tiempo real
    val viaje by viewModel.getTripById(viajeIdLong).collectAsState(initial = null)

    // Obtener los itinerarios en tiempo real
    val itinerarios by viewModel.getItinerariesByTripId(viajeIdLong).collectAsState(initial = emptyList())

    // Si el viaje no existe, regresar automáticamente
    if (viaje == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(viaje?.title ?: "Detalles del Viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("viaje/${viajeId}/createItinerario") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir itinerario")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = viaje?.description ?: "", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Itinerarios:", style = MaterialTheme.typography.headlineSmall)

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(itinerarios) { itinerary ->
                    ItineraryCard(itinerary, navController, viajeId, viewModel)
                }
            }}

        }
    }


@Composable
fun ItineraryCard(itinerary: ItineraryItemEntity, navController: NavController, viajeId: String, viewModel: TravelViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("viaje/${viajeId}/itinerario/${itinerary.id}") },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itinerary.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)

            IconButton(onClick = { viewModel.deleteItinerary(itinerary.id) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Itinerario")
            }
        }
    }
}

//ESTE FUNCIONA BIEN
