package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItineraryScreen(navController: NavController, itineraryViewModel: ItineraryViewModel) {
    var itineraryName by remember { mutableStateOf(TextFieldValue("")) }
    var itineraryDescription by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Itinerario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (itineraryName.text.isNotEmpty()) {
                                itineraryViewModel.addItinerary(itineraryName.text, itineraryDescription.text)
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo para el Nombre del Itinerario
                OutlinedTextField(
                    value = itineraryName,
                    onValueChange = { itineraryName = it },
                    label = { Text("Nombre del Itinerario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la Descripción del Itinerario
                OutlinedTextField(
                    value = itineraryDescription,
                    onValueChange = { itineraryDescription = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para Guardar el Itinerario
                Button(
                    onClick = {
                        if (itineraryName.text.isNotEmpty()) {
                            itineraryViewModel.addItinerary(itineraryName.text, itineraryDescription.text)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Guardar Itinerario")
                }
            }
        }
    )
}
