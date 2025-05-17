package com.example.FlyHigh.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.FlyHigh.data.local.entity.ReservationEntity
import com.example.FlyHigh.data.local.entity.TripEntity
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.domain.model.ReserveRequest
import com.example.FlyHigh.domain.model.Room
import com.example.FlyHigh.ui.viewmodel.HotelViewModel
import com.example.FlyHigh.ui.viewmodel.ReservationViewModel
import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    navController: NavController,
    hotelId: String,
    startDate: String,
    endDate: String,
    viewModel: HotelViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // ViewModels
    val travelViewModel = hiltViewModel<TravelViewModel>()
    val reservationViewModel = hiltViewModel<ReservationViewModel>()

    // State for hotel data
    val availabilityResults by viewModel.availabilityResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Booking states
    var guestName by remember { mutableStateOf("") }
    var guestEmail by remember { mutableStateOf("") }
    var selectedRoomId by remember { mutableStateOf<String?>(null) }
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var isBookingInProgress by remember { mutableStateOf(false) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var reservationId by remember { mutableStateOf<String?>(null) }

    // Trip selector
    val trips by travelViewModel.getAllTrips().collectAsState(initial = emptyList())
    var selectedTrip by remember { mutableStateOf<TripEntity?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Load hotel data
    LaunchedEffect(hotelId, startDate, endDate) {
        viewModel.checkAvailability(startDate = startDate, endDate = endDate, hotelId = hotelId)
    }

    // Show error
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    val hotel = availabilityResults?.hotels?.firstOrNull { it.id == hotelId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(hotel?.name ?: "Hotel Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                hotel?.let {
                    HotelDetailContent(
                        hotel = it,
                        startDate = startDate,
                        endDate = endDate,
                        onRoomSelected = { room ->
                            selectedRoom = room
                            selectedRoomId = room.id
                            guestName = ""
                            guestEmail = ""
                            nameError = null
                            emailError = null
                            showBookingDialog = true
                        }
                    )
                } ?: Text("Hotel not found", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    if (showBookingDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isBookingInProgress) showBookingDialog = false
            },
            title = {
                Text("Complete Your Booking", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    selectedRoom?.let { room ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(room.roomType, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${room.price}€ per night", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = guestName,
                        onValueChange = {
                            guestName = it
                            nameError = if (it.isBlank()) "Name is required" else null
                        },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = nameError != null,
                        supportingText = { nameError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = guestEmail,
                        onValueChange = {
                            guestEmail = it
                            emailError = validateEmail(it)
                        },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        isError = emailError != null,
                        supportingText = { emailError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Asociar a viaje:", style = MaterialTheme.typography.labelMedium)

                    Box {
                        OutlinedTextField(
                            value = selectedTrip?.title ?: "Selecciona un viaje",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            trips.forEach { trip ->
                                DropdownMenuItem(
                                    text = { Text(trip.title) },
                                    onClick = {
                                        selectedTrip = trip
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text("Stay Period", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text("$startDate to $endDate", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        nameError = if (guestName.isBlank()) "Name is required" else null
                        emailError = validateEmail(guestEmail)

                        if (nameError == null && emailError == null && !isBookingInProgress) {
                            isBookingInProgress = true
                            selectedRoomId?.let { roomId ->
                                coroutineScope.launch {
                                    try {
                                        viewModel.checkAvailability(startDate, endDate, hotelId)
                                        delay(500)

                                        val isStillAvailable = availabilityResults?.hotels
                                            ?.firstOrNull { it.id == hotelId }
                                            ?.rooms?.any { it.id == roomId } ?: false

                                        if (!isStillAvailable) {
                                            Toast.makeText(context, "This room is no longer available.", Toast.LENGTH_LONG).show()
                                            isBookingInProgress = false
                                            showBookingDialog = false
                                            return@launch
                                        }

                                        val request = ReserveRequest(
                                            hotelId = hotelId,
                                            roomId = roomId,
                                            startDate = startDate,
                                            endDate = endDate,
                                            guestName = guestName,
                                            guestEmail = guestEmail
                                        )

                                        val resId = viewModel.reserveRoom(request)

                                        if (resId != null) {
                                            reservationId = resId
                                            showBookingDialog = false
                                            showSuccessDialog = true

                                            reservationViewModel.addReservation(
                                                ReservationEntity(
                                                    id = 0,
                                                    tripId = selectedTrip?.id ?: 0L,
                                                    hotelName = hotel?.name ?: "",
                                                    roomType = selectedRoom?.roomType ?: "",
                                                    checkIn = startDate,
                                                    checkOut = endDate,
                                                    imageUrl = selectedRoom?.images?.firstOrNull(),
                                                    reservationId = resId
                                                )
                                            )
                                        } else {
                                            val msg = viewModel.error.value.takeIf { it.isNotEmpty() } ?: "Booking failed"
                                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, e.message ?: "Booking failed", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isBookingInProgress = false
                                    }
                                }
                            }
                        }
                    },
                    enabled = !isBookingInProgress,
                    modifier = Modifier.fillMaxWidth(0.75f)
                ) {
                    if (isBookingInProgress) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Confirm Booking")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { if (!isBookingInProgress) showBookingDialog = false },
                    enabled = !isBookingInProgress
                ) {
                    Text("Cancel")
                }
            }
        )
    }



// Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Booking Confirmed!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Reservation Details",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Divider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Reservation ID:")
                                Text(
                                    text = reservationId ?: "",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Guest Name:")
                                Text(
                                    text = guestName,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Check-in:")
                                Text(
                                    text = startDate,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Check-out:")
                                Text(
                                    text = endDate,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Text("A confirmation email has been sent to:")
                    Text(
                        text = guestEmail,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Thank you for booking with FlyHigh!",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Return to Home")
                }
            }
        )
    }
}

@Composable
fun HotelDetailContent(
    hotel: Hotel,
    startDate: String,
    endDate: String,
    onRoomSelected: (Room) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Hotel Image
        item {
            Box {
                AsyncImage(
                    model = hotel.imageUrl,
                    contentDescription = hotel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Rating chip
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        repeat(hotel.rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFCC00),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Hotel Info
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Hotel name
                Text(
                    text = hotel.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address
                Text(
                    text = hotel.address,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Check-in",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = startDate,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Column {
                            Text(
                                text = "Check-out",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = endDate,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Available Rooms section
                Text(
                    text = "Available Rooms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        // Room list
        items(hotel.rooms) { room ->
            RoomItem(room = room, onRoomSelected = { onRoomSelected(room) })
        }
    }
}

@Composable
fun RoomItem(
    room: Room,
    onRoomSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Room type and price in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = room.roomType,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "${room.price}€ / night",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Room image if available
            if (room.images.isNotEmpty()) {
                AsyncImage(
                    model = room.images.first(),
                    contentDescription = room.roomType,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show room capacity or other info here if needed
            Spacer(modifier = Modifier.height(8.dp))

            // Book now button
            Button(
                onClick = onRoomSelected,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Book Now")
            }
        }
    }
}

// Helper function to validate email format
private fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email is required"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
        else -> null
    }
}