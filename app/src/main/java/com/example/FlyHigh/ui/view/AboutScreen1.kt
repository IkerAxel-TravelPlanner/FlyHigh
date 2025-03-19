package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.FlyHigh.R

@Composable
fun AboutScreen1(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.about_us),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón "Información"
        ElevatedButton(
            onClick = { navController.navigate("about/details") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = stringResource(id = R.string.information), color = Color.White)
        }

        // Botón "Términos y Condiciones"
        ElevatedButton(
            onClick = { navController.navigate("about/terms") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = stringResource(id = R.string.terms_conditions), color = Color.White)
        }

        // Botón "Política de Privacidad"
        ElevatedButton(
            onClick = { navController.navigate("about/privacy") },
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = stringResource(id = R.string.privacy_policy), color = Color.White)
        }
    }
}
