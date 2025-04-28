package com.example.FlyHigh.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ForgotPasswordScreen(navController: NavController, prefilledEmail: String = "") {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    // States
    var email by remember { mutableStateOf(prefilledEmail) }
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var resetEmailSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen title
        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF6200EE)
        )

        if (!resetEmailSent) {
            // Instruction text
            Text(
                text = "Ingresa tu dirección de correo electrónico y te enviaremos un enlace para restablecer tu contraseña.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Send reset email button
            ElevatedButton(
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                auth.sendPasswordResetEmail(email).await()
                                isLoading = false
                                resetEmailSent = true
                                Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = e.message ?: "Error al enviar el correo de recuperación"
                                showErrorDialog = true
                            }
                        }
                    } else {
                        errorMessage = "Por favor, introduce un email válido"
                        showErrorDialog = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Enviar Correo de Recuperación", color = Color.White)
                }
            }

            // Cancel button
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Volver al Inicio de Sesión",
                    color = Color(0xFF6200EE),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Success message
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.Email,
                contentDescription = "Email enviado",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
                tint = Color(0xFF6200EE)
            )

            Text(
                text = "¡Correo Enviado!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF6200EE)
            )

            Text(
                text = "Se ha enviado un correo de recuperación a $email. Por favor, revisa tu bandeja de entrada y sigue las instrucciones para restablecer tu contraseña.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Return to login button
            ElevatedButton(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(text = "Volver al Inicio de Sesión", color = Color.White)
            }
        }
    }

    // Error dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}