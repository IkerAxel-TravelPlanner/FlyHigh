package com.example.FlyHigh.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.utils.FormValidationsUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val scrollState = rememberScrollState()

    // Estados para los campos del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Estados para errores de validación
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear una cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¡Únete a FlyHigh!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6200EE),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Crea una cuenta para organizar tus viajes y descubrir nuevos destinos",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null // Limpiar error al cambiar el valor
                    },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError != null,
                    supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    singleLine = true
                )

                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null // Limpiar error al cambiar el valor
                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )

                // Campo de contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null // Limpiar error al cambiar el valor
                    },
                    label = { Text("Contraseña") },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                // Campo de confirmar contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null // Limpiar error al cambiar el valor
                    },
                    label = { Text("Confirmar contraseña") },
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = confirmPasswordError != null,
                    supportingText = { confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro
                ElevatedButton(
                    onClick = {
                        // Validar formulario
                        var isValid = true

                        if (name.trim().isEmpty()) {
                            nameError = "El nombre es obligatorio"
                            isValid = false
                        } else if (name.trim().length < 3) {
                            nameError = "El nombre debe tener al menos 3 caracteres"
                            isValid = false
                        }

                        if (email.trim().isEmpty()) {
                            emailError = "El email es obligatorio"
                            isValid = false
                        } else if (!FormValidationsUtils.isValidEmail(email)) {
                            emailError = "El email no es válido"
                            isValid = false
                        }

                        if (password.isEmpty()) {
                            passwordError = "La contraseña es obligatoria"
                            isValid = false
                        } else if (password.length < 6) {
                            passwordError = "La contraseña debe tener al menos 6 caracteres"
                            isValid = false
                        }

                        if (confirmPassword.isEmpty()) {
                            confirmPasswordError = "Debes confirmar la contraseña"
                            isValid = false
                        } else if (password != confirmPassword) {
                            confirmPasswordError = "Las contraseñas no coinciden"
                            isValid = false
                        }

                        if (isValid) {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    // Crear usuario en Firebase Auth
                                    val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                                    // Actualizar el perfil del usuario con su nombre
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build()

                                    authResult.user?.updateProfile(profileUpdates)?.await()

                                    isLoading = false
                                    Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()

                                    // Navegar a la pantalla principal
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    errorMessage = when {
                                        e.message?.contains("email address is already in use") == true ->
                                            "Este email ya está registrado"
                                        e.message?.contains("network error") == true ->
                                            "Error de conexión. Verifica tu conexión a internet"
                                        else -> e.message ?: "Error al crear la cuenta"
                                    }
                                    showErrorDialog = true
                                }
                            }
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
                        Text("Registrarse", color = Color.White)
                    }
                }

                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿Ya tienes una cuenta? Inicia sesión", color = Color(0xFF6200EE))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Dialogo de error
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Error de registro") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("Aceptar")
                        }
                    }
                )
            }

            // Indicador de carga
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6200EE))
                }
            }
        }
    }
}