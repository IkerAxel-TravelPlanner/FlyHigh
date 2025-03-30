package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
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
fun CreateItineraryScreen(
    navController: NavController,
    travelViewModel: TravelViewModel,
    tripId: Long
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(Date()) }
    var startTime by remember { mutableStateOf<Date?>(null) }
    var endTime by remember { mutableStateOf<Date?>(null) }
    var type by remember { mutableStateOf(TextFieldValue("")) }

    // Controles para los DatePickers y TimePickers
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Formato para mostrar las fechas y horas
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Estado para validación
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
                            if (validateForm(title.text, location.text)) {
                                travelViewModel.addItinerary(
                                    tripId,
                                    title.text,
                                    description.text,
                                    location.text,
                                    date,
                                    startTime,
                                    endTime,
                                    type.text
                                )
                                navController.popBackStack()
                            } else {
                                hasError = true
                                errorMessage = "Por favor, complete los campos obligatorios"
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
                    value = title,
                    onValueChange = {
                        title = it
                        hasError = false
                    },
                    label = { Text("Título *") },
                    isError = hasError && title.text.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                        hasError = false
                    },
                    label = { Text("Ubicación *") },
                    isError = hasError && location.text.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de fecha
                OutlinedTextField(
                    value = TextFieldValue(dateFormatter.format(date)),
                    onValueChange = { },
                    label = { Text("Fecha *") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tiempo de inicio
                OutlinedTextField(
                    value = TextFieldValue(startTime?.let { timeFormatter.format(it) } ?: ""),
                    onValueChange = { },
                    label = { Text("Hora de inicio") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartTimePicker = true }) {
                            Icon(Icons.Filled.Schedule, contentDescription = "Seleccionar Hora de Inicio")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tiempo de fin
                OutlinedTextField(
                    value = TextFieldValue(endTime?.let { timeFormatter.format(it) } ?: ""),
                    onValueChange = { },
                    label = { Text("Hora de fin") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndTimePicker = true }) {
                            Icon(Icons.Filled.Schedule, contentDescription = "Seleccionar Hora de Fin")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipo de evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (validateForm(title.text, location.text)) {
                            travelViewModel.addItinerary(
                                tripId,
                                title.text,
                                description.text,
                                location.text,
                                date,
                                startTime,
                                endTime,
                                type.text
                            )
                            navController.popBackStack()
                        } else {
                            hasError = true
                            errorMessage = "Por favor, complete los campos obligatorios"
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

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = date.time
                ),
                onDateSelected = { millis ->
                    millis?.let {
                        date = Date(it)
                    }
                }
            )
        }
    }

    // TimePicker Dialog para hora de inicio
    if (showStartTimePicker) {
        val initialHour = startTime?.let { Calendar.getInstance().apply { time = it }.get(Calendar.HOUR_OF_DAY) } ?: 9
        val initialMinute = startTime?.let { Calendar.getInstance().apply { time = it }.get(Calendar.MINUTE) } ?: 0

        TimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                Button(onClick = { showStartTimePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showStartTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            TimePicker(
                state = rememberTimePickerState(
                    initialHour = initialHour,
                    initialMinute = initialMinute
                ),
                onTimeSelected = { hour, minute ->
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    startTime = calendar.time
                }
            )
        }
    }

    // TimePicker Dialog para hora de fin
    if (showEndTimePicker) {
        val initialHour = endTime?.let { Calendar.getInstance().apply { time = it }.get(Calendar.HOUR_OF_DAY) } ?: 18
        val initialMinute = endTime?.let { Calendar.getInstance().apply { time = it }.get(Calendar.MINUTE) } ?: 0

        TimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                Button(onClick = { showEndTimePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showEndTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            TimePicker(
                state = rememberTimePickerState(
                    initialHour = initialHour,
                    initialMinute = initialMinute
                ),
                onTimeSelected = { hour, minute ->
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    endTime = calendar.time
                }
            )
        }
    }
}

// Función de validación del formulario
private fun validateForm(title: String, location: String): Boolean {
    return title.isNotEmpty() && location.isNotEmpty()
}

// Componente DatePicker personalizado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
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

// Componente TimePicker personalizado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    state: TimePickerState,
    onTimeSelected: (Int, Int) -> Unit
) {
    TimePicker(state = state)

    // Detectar cambios y actualizar
    LaunchedEffect(state.hour, state.minute) {
        onTimeSelected(state.hour, state.minute)
    }
}

// Diálogo para el selector de tiempo
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        text = { content() }
    )
}