package com.example.FlyHigh.ui.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.FlyHigh.data.local.entity.ItineraryItemEntity
import com.example.FlyHigh.ui.viewmodel.ReservationViewModel
import com.example.FlyHigh.ui.viewmodel.TravelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDetailScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    reservationViewModel: ReservationViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    viajeId: String
) {
    val context = LocalContext.current
    val viajeIdLong = viajeId.toLongOrNull() ?: -1L

    val viaje by viewModel.getTripById(viajeIdLong).collectAsState(initial = null)
    val itinerarios by viewModel.getItinerariesByTripId(viajeIdLong).collectAsState(initial = emptyList())
    val tripImages by viewModel.tripImages.observeAsState(emptyList())
    val reservations by reservationViewModel.getReservationByTripId(viajeIdLong).collectAsState(initial = null)

    var fullScreenImage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viajeIdLong) {
        viewModel.loadImagesForTrip(viajeIdLong)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.saveImageForTrip(viajeIdLong, uri.toString())
        }
    }

    if (viaje == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(viaje!!.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("viaje/${viajeId}/createItinerario")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir itinerario")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = viaje!!.description, style = MaterialTheme.typography.bodyLarge)
            }

            item {
                Button(
                    onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar imágenes")
                }
            }

            if (tripImages.isNotEmpty()) {
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(tripImages) { image ->
                            Box {
                                AsyncImage(
                                    model = Uri.parse(image.uri),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { fullScreenImage = image.uri }
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.deleteImageForTrip(viajeIdLong, image.uri)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50))
                                        .padding(4.dp)
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            if (reservations != null) {
                item {
                    Text("Reserva de Hotel", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            reservations?.let { reservation ->
                                Text("Hotel: ${reservation.hotelName}", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Habitación: ${reservation.roomType}")
                                Text("Check-in: ${reservation.checkIn}")
                                Text("Check-out: ${reservation.checkOut}")
                                reservation.imageUrl?.let { url ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "Hotel Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { fullScreenImage = url }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(text = "Itinerarios:", style = MaterialTheme.typography.headlineSmall)
            }

            items(itinerarios) { itinerary ->
                ItineraryCard(itinerary, navController, viajeId, viewModel)
            }
        }

        fullScreenImage?.let { uri ->
            AlertDialog(
                onDismissRequest = { fullScreenImage = null },
                confirmButton = {},
                text = {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        }
    }
}

@Composable
fun ItineraryCard(
    itinerary: ItineraryItemEntity,
    navController: NavController,
    viajeId: String,
    viewModel: TravelViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("viaje/${viajeId}/itinerario/${itinerary.id}/detail")
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = itinerary.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = itinerary.description, style = MaterialTheme.typography.bodyMedium)
                }

                Row {
                    IconButton(onClick = {
                        navController.navigate("viaje/${viajeId}/itinerario/${itinerary.id}")
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar Itinerario")
                    }
                    IconButton(onClick = {
                        viewModel.deleteItinerary(itinerary.id)
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar Itinerario")
                    }
                }
            }
        }
    }
}
