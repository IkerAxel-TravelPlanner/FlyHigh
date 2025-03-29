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
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTravelScreen(navController: NavController, travelViewModel: TravelViewModel) {
    var travelName by remember { mutableStateOf(TextFieldValue("")) }
    var travelDestination by remember { mutableStateOf(TextFieldValue("")) }
    var travelDescription by remember { mutableStateOf(TextFieldValue("")) }
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    var imageUrl by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (travelName.text.isNotEmpty() && travelDestination.text.isNotEmpty()) {
                                travelViewModel.addTravel(
                                    travelName.text,
                                    travelDestination.text,
                                    startDate,
                                    endDate,
                                    travelDescription.text,
                                    imageUrl.text.ifEmpty { null }
                                )
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
                OutlinedTextField(
                    value = travelName,
                    onValueChange = { travelName = it },
                    label = { Text("Nombre del Viaje") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = travelDestination,
                    onValueChange = { travelDestination = it },
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = travelDescription,
                    onValueChange = { travelDescription = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de la imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (travelName.text.isNotEmpty() && travelDestination.text.isNotEmpty()) {
                            travelViewModel.addTravel(
                                travelName.text,
                                travelDestination.text,
                                startDate,
                                endDate,
                                travelDescription.text,
                                imageUrl.text.ifEmpty { null }
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Guardar Viaje")
                }
            }
        }
    )
}
