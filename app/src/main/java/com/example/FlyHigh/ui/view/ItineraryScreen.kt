package com.example.FlyHigh.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(navController: NavController, viewModel: TravelViewModel, viajeId: String) {
    val viajeIdLong = viajeId.toLongOrNull() ?: return
    val itinerarios by viewModel.getItinerariesByTripId(viajeIdLong).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Itinerario del Viaje", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("viaje/$viajeId/createItinerario") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Itinerario", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (itinerarios.isEmpty()) {
                Text(
                    "No hay itinerarios disponibles.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
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
            .clickable { navController.navigate("viaje/$viajeId/itinerario/${itinerary.id}/detail") },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itinerary.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateString = dateFormat.format(itinerary.date)

                Text(text = itinerary.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Text(text = dateString, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { navController.navigate("viaje/$viajeId/itinerario/${itinerary.id}") }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Itinerario", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { viewModel.deleteItinerary(itinerary.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Itinerario", tint = Color.Red)
                }
            }
        }
    }
}
