package com.example.FlyHigh.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R
import androidx.compose.ui.res.stringResource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@Composable
fun LoginScreen2(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    // States for email, password, loading state, and error dialog
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // State for password reset dialog
    var showPasswordResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetEmailSent by remember { mutableStateOf(false) }

    // Check if user is already logged in
    LaunchedEffect(auth) {
        if (auth.currentUser != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen title
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF6200EE)
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
        Spacer(modifier = Modifier.height(12.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Login button
        ElevatedButton(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            auth.signInWithEmailAndPassword(email, password).await()
                            isLoading = false
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = e.message ?: "Authentication failed"
                            showErrorDialog = true
                        }
                    }
                } else {
                    errorMessage = "Please fill in all fields"
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
                Text(text = stringResource(id = R.string.login_button), color = Color.White)
            }
        }

        // Create account button - Ahora navega a la pantalla de registro
        ElevatedButton(
            onClick = { navController.navigate("register") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5)),
            enabled = !isLoading
        ) {
            Text(text = "Crear Cuenta", color = Color.Black)
        }

        // Forgotten password
        TextButton(
            onClick = {
                // Navegamos a la nueva pantalla de recuperación de contraseña y pasamos el email actual como parámetro
                navController.navigate("forgot_password?email=$email")
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF6200EE),
                textAlign = TextAlign.Center
            )
        }
    }

    // Error dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(id = R.string.login_failed_title)) },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text(stringResource(id = R.string.ok_button))
                }
            }
        )
    }

    // Password reset dialog
    if (showPasswordResetDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordResetDialog = false },
            title = { Text("Recuperar Contraseña") },
            text = {
                Column {
                    if (resetEmailSent) {
                        Text("Se ha enviado un correo de recuperación a tu email. Por favor, revisa tu bandeja de entrada.")
                    } else {
                        Text("Ingresa tu dirección de correo electrónico y te enviaremos un enlace para restablecer tu contraseña.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (resetEmailSent) {
                            showPasswordResetDialog = false
                        } else if (resetEmail.isNotEmpty()) {
                            coroutineScope.launch {
                                try {
                                    isLoading = true
                                    auth.sendPasswordResetEmail(resetEmail).await()
                                    resetEmailSent = true
                                    isLoading = false
                                    Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    isLoading = false
                                    errorMessage = e.message ?: "Error al enviar el correo de recuperación"
                                    showErrorDialog = true
                                    showPasswordResetDialog = false
                                }
                            }
                        } else {
                            errorMessage = "Por favor, introduce un email válido"
                            showErrorDialog = true
                            showPasswordResetDialog = false
                        }
                    }
                ) {
                    Text(if (resetEmailSent) "Cerrar" else "Enviar")
                }
            },
            dismissButton = {
                if (!resetEmailSent) {
                    TextButton(onClick = { showPasswordResetDialog = false }) {
                        Text("Cancelar")
                    }
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