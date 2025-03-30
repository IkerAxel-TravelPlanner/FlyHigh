package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItineraryScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    viajeId: Long,
    itineraryId: Long
) {
    // Estado de carga
    var isLoading by remember { mutableStateOf(true) }

    // Obtener los datos de la base de datos a través de un Flow
    val itinerary by viewModel.getItineraryById(itineraryId).collectAsState(initial = null)

    // Variables para todos los campos editables
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    // Para formatear y manejar fechas
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    var dateString by remember { mutableStateOf("") }
    var startTimeString by remember { mutableStateOf("") }
    var endTimeString by remember { mutableStateOf("") }

    // Gestor de foco para cerrar el teclado
    val focusManager = LocalFocusManager.current

    // Inicializar campos cuando se carga el itinerario
    LaunchedEffect(itinerary) {
        if (itinerary != null) {
            title = itinerary!!.title
            description = itinerary!!.description
            location = itinerary!!.location
            type = itinerary!!.type
            dateString = dateFormatter.format(itinerary!!.date)

            itinerary!!.startTime?.let {
                startTimeString = timeFormatter.format(it)
            }

            itinerary!!.endTime?.let {
                endTimeString = timeFormatter.format(it)
            }

            isLoading = false
        }
    }

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
        if (isLoading) {
            // Mostrar indicador de carga
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (itinerary == null) {
            // Mostrar mensaje de error
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se pudo encontrar el itinerario")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            }
        } else {
            // Formulario de edición
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del Itinerario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Ubicación
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Tipo
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipo de actividad") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Fecha
                OutlinedTextField(
                    value = dateString,
                    onValueChange = { dateString = it },
                    label = { Text("Fecha (dd/mm/yyyy)") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Hora de inicio
                OutlinedTextField(
                    value = startTimeString,
                    onValueChange = { startTimeString = it },
                    label = { Text("Hora de inicio (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Hora de fin
                OutlinedTextField(
                    value = endTimeString,
                    onValueChange = { endTimeString = it },
                    label = { Text("Hora de fin (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                // Botón de guardar
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank() && location.isNotBlank()) {
                            // Parsear fechas y horas
                            try {
                                val date = dateFormatter.parse(dateString) ?: itinerary!!.date
                                val startTime = if (startTimeString.isNotBlank()) timeFormatter.parse(startTimeString) else null
                                val endTime = if (endTimeString.isNotBlank()) timeFormatter.parse(endTimeString) else null

                                // Crear objeto actualizado
                                val updatedItinerary = itinerary!!.copy(
                                    title = title,
                                    description = description,
                                    location = location,
                                    type = type,
                                    date = date,
                                    startTime = startTime,
                                    endTime = endTime
                                )

                                // Actualizar en la base de datos
                                viewModel.updateItinerary(updatedItinerary)
                                navController.popBackStack()
                            } catch (e: Exception) {
                                // Manejar errores de formato de fecha/hora
                                // Aquí deberías mostrar un mensaje de error al usuario
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank() && description.isNotBlank() && location.isNotBlank()
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}