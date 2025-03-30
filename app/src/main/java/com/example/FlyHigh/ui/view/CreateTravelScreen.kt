package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import java.text.SimpleDateFormat
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

    // Variables para controlar la visualización de los DatePickers
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Formato para mostrar las fechas
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Estado para validación
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Inicializar fechas predeterminadas: hoy para inicio y una semana después para fin
    LaunchedEffect(Unit) {
        val calendar = Calendar.getInstance()
        startDate = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 7)
        endDate = calendar.time
    }

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
                            if (validateForm(travelName.text, travelDestination.text, startDate, endDate)) {
                                travelViewModel.addTravel(
                                    travelName.text,
                                    travelDestination.text,
                                    startDate,
                                    endDate,
                                    travelDescription.text,
                                    imageUrl.text.ifEmpty { null }
                                )
                                navController.popBackStack()
                            } else {
                                hasError = true
                                errorMessage = if (startDate.after(endDate)) {
                                    "La fecha de inicio no puede ser posterior a la fecha de fin"
                                } else {
                                    "Por favor, complete los campos obligatorios"
                                }
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
                // Mensaje de error
                if (hasError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Campos del formulario
                OutlinedTextField(
                    value = travelName,
                    onValueChange = {
                        travelName = it
                        hasError = false
                    },
                    label = { Text("Nombre del Viaje *") },
                    isError = hasError && travelName.text.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = travelDestination,
                    onValueChange = {
                        travelDestination = it
                        hasError = false
                    },
                    label = { Text("Destino *") },
                    isError = hasError && travelDestination.text.isEmpty(),
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

                // Fecha de inicio
                OutlinedTextField(
                    value = TextFieldValue(dateFormatter.format(startDate)),
                    onValueChange = { },
                    label = { Text("Fecha de inicio *") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha de Inicio")
                        }
                    },
                    isError = hasError && startDate.after(endDate),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fecha de fin
                OutlinedTextField(
                    value = TextFieldValue(dateFormatter.format(endDate)),
                    onValueChange = { },
                    label = { Text("Fecha de fin *") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha de Fin")
                        }
                    },
                    isError = hasError && startDate.after(endDate),
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
                        if (validateForm(travelName.text, travelDestination.text, startDate, endDate)) {
                            travelViewModel.addTravel(
                                travelName.text,
                                travelDestination.text,
                                startDate,
                                endDate,
                                travelDescription.text,
                                imageUrl.text.ifEmpty { null }
                            )
                            navController.popBackStack()
                        } else {
                            hasError = true
                            errorMessage = if (startDate.after(endDate)) {
                                "La fecha de inicio no puede ser posterior a la fecha de fin"
                            } else {
                                "Por favor, complete los campos obligatorios"
                            }
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

    // DatePicker Dialog para fecha de inicio
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                Button(onClick = { showStartDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showStartDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            TravelDatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = startDate.time),
                onDateSelected = { millis ->
                    millis?.let {
                        startDate = Date(it)
                        // Validar que la fecha de inicio no sea posterior a la de fin
                        if (startDate.after(endDate)) {
                            // Ajustar la fecha de fin para que sea igual o posterior a la de inicio
                            val calendar = Calendar.getInstance()
                            calendar.time = startDate
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                            endDate = calendar.time
                        }
                    }
                }
            )
        }
    }

    // DatePicker Dialog para fecha de fin
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                Button(onClick = { showEndDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showEndDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = endDate.time),
                onDateSelected = { millis ->
                    millis?.let {
                        val newEndDate = Date(it)
                        // Solo actualizar si la fecha de fin no es anterior a la de inicio
                        if (!newEndDate.before(startDate)) {
                            endDate = newEndDate
                        } else {
                            // Podríamos mostrar un mensaje de error aquí o simplemente no actualizar
                            hasError = true
                            errorMessage = "La fecha de fin no puede ser anterior a la fecha de inicio"
                        }
                    }
                }
            )
        }
    }
}

// Función de validación del formulario
private fun validateForm(title: String, destination: String, startDate: Date, endDate: Date): Boolean {
    return title.isNotEmpty() &&
            destination.isNotEmpty() &&
            !startDate.after(endDate)
}

// Option A: Rename the function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TravelDatePicker(
    state: DatePickerState,
    onDateSelected: (Long?) -> Unit
) {
    DatePicker(
        state = state,
        title = { Text("Selecciona una fecha") },
        showModeToggle = true
    )

    // Detectar cambios y actualizar
    LaunchedEffect(state.selectedDateMillis) {
        onDateSelected(state.selectedDateMillis)
    }
}