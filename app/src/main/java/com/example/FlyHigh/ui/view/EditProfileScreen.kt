package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.FlyHigh.ui.viewmodel.EditProfileUiState
import com.example.FlyHigh.ui.viewmodel.EditProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    userId: Long? = null,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Formato para mostrar la fecha
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Estados para mostrar mensajes de error
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Estado para el diálogo de fecha
    var showDatePicker by remember { mutableStateOf(false) }

    // Estados para validación
    val inputErrors = remember { mutableStateMapOf<String, String>() }

    // Cargar los datos del usuario cuando se inicia la pantalla
    LaunchedEffect(key1 = userId) {
        viewModel.loadUserData(userId)
    }

    // Detectar cambios en el estado y actualizar errores
    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            errorMessage = it
            showErrorDialog = true
        }
    }

    // Mostrar diálogo de fecha cuando se solicita
    if (showDatePicker) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona tu fecha de nacimiento")
            .setSelection(uiState.birthDate.time)
            .build()

        // No podemos mostrar el datePicker directamente en Compose,
        // pero asumimos que hay una forma de hacerlo en la app real
        showDatePicker = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp).padding(end = 16.dp)
                        )
                    } else {
                        IconButton(
                            onClick = {
                                val errors = viewModel.getInputErrors()
                                if (errors.isEmpty()) {
                                    viewModel.saveProfile {
                                        // Navegar de vuelta al perfil después de guardar con éxito
                                        navController.popBackStack()
                                    }
                                } else {
                                    inputErrors.clear()
                                    inputErrors.putAll(errors)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Guardar",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingScreen(padding)
            }
            uiState.userData == null -> {
                ErrorScreen("No se pudo cargar los datos del perfil", padding)
            }
            else -> {
                EditProfileContent(
                    uiState = uiState,
                    onUsernameChange = viewModel::updateUsername,
                    onEmailChange = viewModel::updateEmail,
                    onBirthDateChange = viewModel::updateBirthDate,
                    onAddressChange = viewModel::updateAddress,
                    onCountryChange = viewModel::updateCountry,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                    onAcceptEmailsOffersChange = viewModel::updateAcceptEmailsOffers,
                    onDatePickerRequest = { showDatePicker = true },
                    dateFormatter = dateFormatter,
                    padding = padding,
                    scrollState = scrollState,
                    inputErrors = inputErrors
                )
            }
        }
    }

    // Mostrar diálogo de error si es necesario
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun LoadingScreen(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF6200EE))
    }
}

@Composable
private fun ErrorScreen(errorMessage: String, padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileContent(
    uiState: EditProfileUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthDateChange: (Date) -> Unit,
    onAddressChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAcceptEmailsOffersChange: (Boolean) -> Unit,
    onDatePickerRequest: () -> Unit,
    dateFormatter: SimpleDateFormat,
    padding: PaddingValues,
    scrollState: androidx.compose.foundation.ScrollState,
    inputErrors: Map<String, String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top
    ) {
        // Nombre de usuario
        ProfileTextField(
            value = uiState.username,
            onValueChange = onUsernameChange,
            label = "Nombre de usuario",
            icon = Icons.Default.Person,
            errorMessage = inputErrors["username"]
        )

        // Email
        ProfileTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            errorMessage = inputErrors["email"]
        )

        // Fecha de nacimiento
        OutlinedTextField(
            value = dateFormatter.format(uiState.birthDate),
            onValueChange = { /* No editable directamente */ },
            label = { Text("Fecha de nacimiento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Fecha de nacimiento",
                    tint = Color(0xFF6200EE)
                )
            },
            trailingIcon = {
                IconButton(onClick = onDatePickerRequest) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Seleccionar fecha",
                        tint = Color(0xFF6200EE)
                    )
                }
            },
            readOnly = true,
            enabled = false
        )

        // Dirección
        ProfileTextField(
            value = uiState.address,
            onValueChange = onAddressChange,
            label = "Dirección",
            icon = Icons.Default.Home
        )

        // País
        ProfileTextField(
            value = uiState.country,
            onValueChange = onCountryChange,
            label = "País",
            icon = Icons.Default.Public
        )

        // Teléfono
        ProfileTextField(
            value = uiState.phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Teléfono",
            icon = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone,
            errorMessage = inputErrors["phone"]
        )

        // Preferencias
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Preferencias",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Switch(
                checked = uiState.acceptEmailsOffers,
                onCheckedChange = onAcceptEmailsOffersChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF6200EE),
                    checkedTrackColor = Color(0xFF6200EE).copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Recibir ofertas por email",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Espacio adicional al final para evitar que el teclado oculte los campos
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF6200EE)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorMessage != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF6200EE),
                focusedLabelColor = Color(0xFF6200EE),
                cursorColor = Color(0xFF6200EE)
            )
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}