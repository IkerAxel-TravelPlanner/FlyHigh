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
import com.example.FlyHigh.ui.viewmodel.TravelViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItineraryScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    viajeId: Long,
    itineraryId: Long
) {
    val viaje by viewModel.getTripById(viajeId).collectAsState(initial = null)
    val itinerary by viewModel.getItineraryById(itineraryId).collectAsState(initial = null)

    if (viaje == null || itinerary == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var name by remember { mutableStateOf(itinerary!!.title) }
    var description by remember { mutableStateOf(itinerary!!.description) }

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
                label = { Text("Nombre del Itinerario") },
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
                        viewModel.updateItinerary(
                            itinerary!!.copy(title = name, description = description)
                        )
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
