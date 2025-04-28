package com.example.FlyHigh.ui.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentUser = auth.currentUser

    // Estado para cargar datos
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Estados para los campos del formulario
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf(calendar.time) }
    var birthDateString by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var acceptEmailsOffers by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf(0L) }
    var firebaseUid by remember { mutableStateOf("") }

    // Estados para errores de validación
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var countryError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }

    // Cargar datos del usuario de Firestore
    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid != null) {
            try {
                val userSnapshot = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                if (userSnapshot.exists()) {
                    val user = userSnapshot.toObject(User::class.java)
                    user?.let {
                        username = it.username
                        email = it.email
                        birthDate = it.birthDate
                        birthDateString = dateFormatter.format(it.birthDate)
                        address = it.address
                        country = it.country
                        phoneNumber = it.phoneNumber
                        acceptEmailsOffers = it.acceptEmailsOffers
                        userId = it.id
                        firebaseUid = it.firebaseUid ?: ""
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar datos: ${e.message}"
                showErrorDialog = true
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
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
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6200EE))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Imagen de perfil (simulada como un círculo)
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 16.dp),
                        shape = MaterialTheme.shapes.large,
                        color = Color.Gray
                    ) {
                        // Aquí se puede agregar una imagen de perfil real
                    }

                    // Campo de nombre de usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            usernameError = null
                        },
                        label = { Text("Nombre completo") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = usernameError != null,
                        supportingText = { usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        singleLine = true
                    )

                    // Campo de email (no editable si está autenticado con Firebase)
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError != null,
                        supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        readOnly = true,  // No permitimos cambiar el email por simplificación
                        enabled = false
                    )

                    // Campo de fecha de nacimiento
                    OutlinedTextField(
                        value = birthDateString,
                        onValueChange = { },
                        label = { Text("Fecha de nacimiento") },
                        leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Fecha de nacimiento") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = birthDateError != null,
                        supportingText = { birthDateError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val birthCal = Calendar.getInstance()
                                    birthCal.time = birthDate
                                    val year = birthCal.get(Calendar.YEAR)
                                    val month = birthCal.get(Calendar.MONTH)
                                    val day = birthCal.get(Calendar.DAY_OF_MONTH)

                                    DatePickerDialog(
                                        context,
                                        { _, selectedYear, selectedMonth, selectedDay ->
                                            calendar.set(selectedYear, selectedMonth, selectedDay)
                                            birthDate = calendar.time
                                            birthDateString = dateFormatter.format(birthDate)
                                            birthDateError = null
                                        },
                                        year,
                                        month,
                                        day
                                    ).show()
                                }
                            ) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                            }
                        }
                    )

                    // Campo de dirección
                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = null
                        },
                        label = { Text("Dirección") },
                        leadingIcon = { Icon(Icons.Filled.Home, contentDescription = "Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = addressError != null,
                        supportingText = { addressError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp))
                    )

                    // Campo de país
                    OutlinedTextField(
                        value = country,
                        onValueChange = {
                            country = it
                            countryError = null
                        },
                        label = { Text("País") },
                        leadingIcon = { Icon(Icons.Filled.Public, contentDescription = "País") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = countryError != null,
                        supportingText = { countryError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        singleLine = true
                    )

                    // Campo de teléfono
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            phoneNumberError = null
                        },
                        label = { Text("Número de teléfono") },
                        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = phoneNumberError != null,
                        supportingText = { phoneNumberError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                    )

                    // Checkbox para aceptar recibir emails de ofertas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = acceptEmailsOffers,
                            onCheckedChange = { acceptEmailsOffers = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EE))
                        )
                        Text(
                            text = "Quiero recibir ofertas y promociones por email",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones para guardar o cancelar cambios
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Botón cancelar
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                        ) {
                            Text("Cancelar")
                        }

                        // Botón guardar
                        ElevatedButton(
                            onClick = {
                                // Validar formulario
                                var isValid = true

                                if (username.trim().isEmpty()) {
                                    usernameError = "El nombre es obligatorio"
                                    isValid = false
                                } else if (username.trim().length < 3) {
                                    usernameError = "El nombre debe tener al menos 3 caracteres"
                                    isValid = false
                                }

                                if (address.trim().isEmpty()) {
                                    addressError = "La dirección es obligatoria"
                                    isValid = false
                                }

                                if (country.trim().isEmpty()) {
                                    countryError = "El país es obligatorio"
                                    isValid = false
                                }

                                if (phoneNumber.trim().isEmpty()) {
                                    phoneNumberError = "El número de teléfono es obligatorio"
                                    isValid = false
                                }

                                // Validación de mayoría de edad (opcional)
                                val today = Calendar.getInstance()
                                val birthCal = Calendar.getInstance()
                                birthCal.time = birthDate
                                val age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
                                if (age < 18) {
                                    birthDateError = "Debes ser mayor de 18 años"
                                    isValid = false
                                }

                                if (isValid) {
                                    isSaving = true
                                    coroutineScope.launch {
                                        try {
                                            // Guardar datos actualizados en Firestore
                                            val updatedUser = User(
                                                id = userId,
                                                firebaseUid = firebaseUid,
                                                username = username,
                                                email = email,
                                                birthDate = birthDate,
                                                address = address,
                                                country = country,
                                                phoneNumber = phoneNumber,
                                                acceptEmailsOffers = acceptEmailsOffers
                                            )

                                            currentUser?.uid?.let { uid ->
                                                // Actualizar el nombre de usuario en Firebase Auth
                                                currentUser.updateProfile(
                                                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username)
                                                        .build()
                                                ).await()

                                                // Actualizar datos en Firestore
                                                firestore.collection("users")
                                                    .document(uid)
                                                    .set(updatedUser)
                                                    .await()

                                                isSaving = false
                                                showSuccessDialog = true
                                            } ?: run {
                                                throw Exception("Usuario no autenticado")
                                            }
                                        } catch (e: Exception) {
                                            isSaving = false
                                            errorMessage = "Error al guardar cambios: ${e.message}"
                                            showErrorDialog = true
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Guardar", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Dialog de error
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Error") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("Aceptar")
                        }
                    }
                )
            }

            // Dialog de éxito
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    title = { Text("Perfil actualizado") },
                    text = { Text("Los cambios han sido guardados correctamente") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                        ) {
                            Text("Aceptar")
                        }
                    }
                )
            }

            // Overlay de carga
            if (isSaving) {
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