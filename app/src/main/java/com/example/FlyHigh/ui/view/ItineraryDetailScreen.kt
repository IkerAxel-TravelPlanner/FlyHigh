package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryDetailScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    viajeId: String,
    itineraryId: String
) {
    val itineraryIdLong = itineraryId.toLongOrNull() ?: -1L
    val viajeIdLong = viajeId.toLongOrNull() ?: -1L

    // Obtener los datos del itinerario
    val itinerary by viewModel.getItineraryById(itineraryIdLong).collectAsState(initial = null)

    // Estado de carga
    var isLoading by remember { mutableStateOf(true) }

    // Formatear fechas y horas
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    LaunchedEffect(itinerary) {
        if (itinerary != null) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Detalles del Itinerario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón para editar
                    IconButton(onClick = {
                        navController.navigate("viaje/$viajeId/itinerario/$itineraryId")
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
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
            // Mostrar detalles del itinerario
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = itinerary!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Tipo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = itinerary!!.type,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Divider()

                // Descripción
                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = itinerary!!.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider()

                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = itinerary!!.location,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Fecha
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateFormatter.format(itinerary!!.date),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Horario
                if (itinerary!!.startTime != null || itinerary!!.endTime != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Horario",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val startTime = itinerary!!.startTime?.let { timeFormatter.format(it) } ?: "-"
                        val endTime = itinerary!!.endTime?.let { timeFormatter.format(it) } ?: "-"
                        Text(
                            text = "De $startTime a $endTime",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}