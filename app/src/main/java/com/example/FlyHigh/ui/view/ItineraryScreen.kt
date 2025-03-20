package com.example.FlyHigh.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.Itinerary
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(navController: NavController, viewModel: ItineraryViewModel) {
    val itineraries by remember { derivedStateOf { viewModel.itineraries } }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Itinerarios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createItinerary") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Itinerario")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (itineraries.isEmpty()) {
                Text("No hay itinerarios disponibles.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(itineraries) { itinerary ->
                        ItineraryItem(itinerary, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ItineraryItem(itinerary: Itinerary, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("editItinerary/${itinerary.id}") },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itinerary.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
