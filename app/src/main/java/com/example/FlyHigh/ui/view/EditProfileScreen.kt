package com.example.FlyHigh.ui.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.FlyHigh.R
import com.example.FlyHigh.ui.viewmodel.EditProfileUiState
import com.example.FlyHigh.ui.viewmodel.EditProfileViewModel
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

    // Formato para mostrar la fecha
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Estados para mostrar mensajes de error
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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

    val primaryColor = Color(0xFF6200EE)
    val surfaceColor = Color(0xFFF5F5F5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar Perfil",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }
                uiState.userData == null -> {
                    ErrorScreen("No se pudo cargar los datos del perfil")
                }
                else -> {
                    EditProfileContent(
                        uiState = uiState,
                        onUsernameChange = viewModel::updateUsername,
                        onBirthDateChange = viewModel::updateBirthDate,
                        onAddressChange = viewModel::updateAddress,
                        onCountryChange = viewModel::updateCountry,
                        onPhoneNumberChange = viewModel::updatePhoneNumber,
                        onAcceptEmailsOffersChange = viewModel::updateAcceptEmailsOffers,
                        context = context,
                        dateFormatter = dateFormatter,
                        scrollState = scrollState,
                        inputErrors = inputErrors,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

            // Success Snackbar
            if (uiState.saveSuccess) {
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    action = {
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text("Perfil actualizado correctamente")
                }
            }
        }
    }

    // Mostrar diálogo de error si es necesario
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error", fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Cargando información del perfil...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorScreen(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
    onBirthDateChange: (Date) -> Unit,
    onAddressChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAcceptEmailsOffersChange: (Boolean) -> Unit,
    context: android.content.Context,
    dateFormatter: SimpleDateFormat,
    scrollState: androidx.compose.foundation.ScrollState,
    inputErrors: Map<String, String>,
    viewModel: EditProfileViewModel,
    navController: NavController
) {
    val primaryColor = Color(0xFF6200EE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Sección de información personal
        SectionHeader(title = "Información Personal", icon = Icons.Default.Person)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Nombre de usuario
                ProfileTextField(
                    value = uiState.username,
                    onValueChange = onUsernameChange,
                    label = "Nombre de usuario",
                    icon = Icons.Default.AccountCircle,
                    errorMessage = inputErrors["username"],
                    primaryColor = primaryColor
                )

                // Email (no editable)
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { /* No editable */ },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = primaryColor.copy(alpha = 0.7f)
                        )
                    },
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = LocalContentColor.current.copy(alpha = 0.8f),
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        disabledLeadingIconColor = primaryColor.copy(alpha = 0.7f)
                    )
                )

                // Fecha de nacimiento con DatePicker
                val calendar = Calendar.getInstance()
                calendar.time = uiState.birthDate

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    context,
                    R.style.DatePickerTheme,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        calendar.set(selectedYear, selectedMonth, selectedDay)
                        onBirthDateChange(calendar.time)
                    },
                    year, month, day
                )

                // Establecer límites de fecha (por ejemplo, no menores de 18 años)
                val maxDate = Calendar.getInstance()
                maxDate.add(Calendar.YEAR, -18)
                datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

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
                            tint = primaryColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Seleccionar fecha",
                                tint = primaryColor
                            )
                        }
                    },
                    readOnly = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
            }
        }

        // Sección de contacto
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Información de Contacto", icon = Icons.Default.Contacts)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Dirección
                ProfileTextField(
                    value = uiState.address,
                    onValueChange = onAddressChange,
                    label = "Dirección",
                    icon = Icons.Default.Home,
                    primaryColor = primaryColor
                )

                // País
                ProfileTextField(
                    value = uiState.country,
                    onValueChange = onCountryChange,
                    label = "País",
                    icon = Icons.Default.Public,
                    primaryColor = primaryColor
                )

                // Teléfono
                ProfileTextField(
                    value = uiState.phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    label = "Teléfono",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    errorMessage = inputErrors["phone"],
                    primaryColor = primaryColor
                )
            }
        }

        // Sección de preferencias
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Preferencias", icon = Icons.Outlined.Notifications)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Email Ofertas",
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Recibir ofertas por email",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Te informaremos sobre promociones especiales",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Switch(
                    checked = uiState.acceptEmailsOffers,
                    onCheckedChange = onAcceptEmailsOffersChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = primaryColor,
                        checkedTrackColor = primaryColor.copy(alpha = 0.5f)
                    )
                )
            }
        }

        // Botón de guardar
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val errors = viewModel.getInputErrors()
                if (errors.isEmpty()) {
                    viewModel.saveProfile {
                        // Navegar de vuelta al perfil después de guardar con éxito
                        navController.popBackStack()
                    }
                } else {
                    // Usar clear y putAll en una MutableMap
                    if (inputErrors is MutableMap) {
                        inputErrors.clear()
                        inputErrors.putAll(errors)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
            }
        }

        // Espacio adicional al final para evitar que el teclado oculte los campos
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF6200EE),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )
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
    errorMessage: String? = null,
    primaryColor: Color
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
                    tint = primaryColor
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorMessage != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            ),
            shape = RoundedCornerShape(8.dp)
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

