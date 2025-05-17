package com.example.FlyHigh.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.FlyHigh.data.local.entity.ReservationEntity
import com.example.FlyHigh.ui.viewmodel.ReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationListScreen(
    navController: NavController,
    viewModel: ReservationViewModel = hiltViewModel()
) {
    val reservations by viewModel.allReservations.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") },
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
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(reservations) { res ->
                    ReservationCard(res, onDelete = {
                        viewModel.cancelReservation(res) { success ->
                            if (!success) {
                                // puedes mostrar un Toast si quieres
                                println("No se pudo cancelar la reserva en la API")
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun ReservationCard(res: ReservationEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (!res.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(res.imageUrl),
                    contentDescription = "Hotel",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = res.hotelName, style = MaterialTheme.typography.titleMedium)
                Text(text = "Tipo habitación: ${res.roomType}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Check-in: ${res.checkIn}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Check-out: ${res.checkOut}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
