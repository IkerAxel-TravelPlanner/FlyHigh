package com.example.FlyHigh.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Barra superior con botón de retroceso
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.explore_title), fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {  // Función para volver atrás
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver atrás")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Encabezado con imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.imagen1), // Cambia a una imagen relevante
            contentDescription = "Explore Header",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Título principal
        Text(
            text = stringResource(id = R.string.explore_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Descripción
        Text(
            text = "Descubre destinos únicos, actividades emocionantes y mucho más para tu próximo viaje.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Sección de acceso rápido con tarjetas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)  // Aseguramos que haya espacio entre las tarjetas
        ) {
            // Uso de weight en Column
            ExploreCard(
                title = "Lugares Icónicos",
                description = "Explora las maravillas del mundo.",
                imageRes = R.drawable.imagen7, // Reemplaza con una imagen adecuada
                onClick = { navController.navigate("explore/places") },
                modifier = Modifier.weight(1f)  // Correctamente aplicando weight
            )

            ExploreCard(
                title = "Actividades Emocionantes",
                description = "Encuentra aventuras únicas para todos los gustos.",
                imageRes = R.drawable.imagen1, // Reemplaza con una imagen adecuada
                onClick = { navController.navigate("explore/activities") },
                modifier = Modifier.weight(1f)  // Correctamente aplicando weight
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Opción para ver más detalles
        Button(
            onClick = { navController.navigate("explore/details") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = R.string.explore_button),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Componente para las tarjetas de acceso rápido (Lugares, Actividades, etc.)
@Composable
fun ExploreCard(
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
