package com.example.FlyHigh.ui.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.FlyHigh.domain.model.Hotel
import com.example.FlyHigh.ui.viewmodel.HotelViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelSearchScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // State for search parameters
    var selectedCity by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(3)) }

    // State for showing date pickers
    val startDateSheetState = rememberSheetState()
    val endDateSheetState = rememberSheetState()

    // Availability results
    val availabilityResults by viewModel.availabilityResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Date picker dialogs
    CalendarDialog(
        state = startDateSheetState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { date ->
            startDate = date
            // Make sure end date is not before start date
            if (endDate.isBefore(date)) {
                endDate = date.plusDays(1)
            }
        }
    )

    CalendarDialog(
        state = endDateSheetState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { date ->
            // Make sure end date is not before start date
            if (!date.isBefore(startDate)) {
                endDate = date
            } else {
                Toast.makeText(context, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Show error if present
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hotel Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // City Selection
            Text(
                text = "Select a City",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Londres", "Paris", "Barcelona").forEach { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = { selectedCity = city },
                        label = { Text(city) },
                        leadingIcon = {
                            if (selectedCity == city) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Selection
            Text(
                text = "Select Dates",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { startDateSheetState.show() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Select start date",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Check-in",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { endDateSheetState.show() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Select end date",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Check-out",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Button
            Button(
                onClick = {
                    if (selectedCity.isNotEmpty()) {
                        viewModel.checkAvailability(
                            startDate = startDate.format(dateFormatter),
                            endDate = endDate.format(dateFormatter),
                            city = selectedCity
                        )
                    } else {
                        Toast.makeText(context, "Please select a city", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCity.isNotEmpty()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search Hotels")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Results section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    availabilityResults?.let { availability ->
                        if (availability.hotels.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hotels available for the selected dates",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Column {
                                Text(
                                    text = "Available Hotels (${availability.hotels.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(availability.hotels) { hotel ->
                                        HotelItem(
                                            hotel = hotel,
                                            startDate = startDate.format(dateFormatter),
                                            endDate = endDate.format(dateFormatter),
                                            onHotelSelected = { selectedHotel ->
                                                // Navigate to hotel detail screen
                                                navController.navigate(
                                                    "hotel_detail/${selectedHotel.id}?startDate=${startDate.format(dateFormatter)}&endDate=${endDate.format(dateFormatter)}"
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } ?: run {
                        // Initial state before any search
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select a city and dates to search for hotels",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotelItem(
    hotel: Hotel,
    startDate: String,
    endDate: String,
    onHotelSelected: (Hotel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHotelSelected(hotel) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Hotel Image
            hotel.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = hotel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
            }

            // Hotel Info
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = hotel.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = hotel.address,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Room info (showing lowest price room)
                hotel.rooms.minByOrNull { it.price }?.let { cheapestRoom ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "From ${cheapestRoom.price}€ / night",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = cheapestRoom.roomType,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}