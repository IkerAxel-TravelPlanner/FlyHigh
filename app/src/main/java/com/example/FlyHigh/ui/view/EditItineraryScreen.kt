package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.Itinerary
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItineraryScreen(navController: NavController, viewModel: ItineraryViewModel, itineraryId: String) {
    // **Buscar el itinerario en la lista**
    val itinerary = remember { viewModel.itineraries.find { it.id == itineraryId } }

    // **Si no se encuentra, salir de la pantalla**
    if (itinerary == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    // **Estados para los campos editables**
    var name by remember { mutableStateOf(itinerary.name) }
    var description by remember { mutableStateOf(itinerary.description) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Editar Itinerario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del viaje") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Ocultar teclado */ })
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Ocultar teclado */ })
            )

            Button(
                onClick = {
                    if (name.isNotBlank() && description.isNotBlank()) {
                        viewModel.updateItinerary(itinerary.copy(name = name, description = description))
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
