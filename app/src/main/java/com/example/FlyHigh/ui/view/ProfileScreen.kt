package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.FlyHigh.domain.model.User
import com.example.FlyHigh.ui.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: Long? = null,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val scrollState = rememberScrollState()
    val currentUser = auth.currentUser
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Estado para el diálogo de cierre de sesión
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Estado de carga y error
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los datos del usuario cuando se inicia la pantalla
    LaunchedEffect(key1 = userId) {
        viewModel.loadUserData(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingScreen(padding)
            }
            uiState.error != null -> {
                ErrorScreen(uiState.error!!, padding)
            }
            uiState.userData != null -> {
                ProfileContent(
                    user = uiState.userData!!,
                    dateFormatter = dateFormatter,
                    isCurrentUserProfile = uiState.isCurrentUserProfile,
                    onEditProfile = { navController.navigate("editProfile/${uiState.userData!!.id}") },
                    onNavigateHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onLogoutRequest = { showLogoutDialog = true },
                    padding = padding,
                    scrollState = scrollState
                )
            }
            else -> {
                EmptyProfileScreen(padding)
            }
        }
    }

    // Diálogo de confirmación para cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                        showLogoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
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

@Composable
private fun EmptyProfileScreen(padding: PaddingValues) {
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
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil vacío",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se pudo cargar la información del perfil",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    dateFormatter: SimpleDateFormat,
    isCurrentUserProfile: Boolean,
    onEditProfile: () -> Unit,
    onNavigateHome: () -> Unit,
    onLogoutRequest: () -> Unit,
    padding: PaddingValues,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = padding.calculateTopPadding())
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto de perfil
        Surface(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
                .clip(CircleShape),
            color = Color(0xFFE0E0E0)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        // Información del usuario
        ProfileInfoSection(user, dateFormatter)

        Spacer(modifier = Modifier.height(24.dp))

        if (isCurrentUserProfile) {
            // Botón para editar el perfil
            ProfileButton(
                text = "Editar Perfil",
                icon = Icons.Filled.Edit,
                color = Color(0xFF6200EE),
                onClick = onEditProfile
            )

            // Botón para volver al inicio
            ProfileButton(
                text = "Volver al Inicio",
                icon = Icons.Filled.Home,
                color = Color(0xFF6200EE),
                onClick = onNavigateHome
            )

            // Botón para cerrar sesión
            ProfileButton(
                text = "Cerrar Sesión",
                icon = Icons.Filled.ExitToApp,
                color = Color(0xFFE91E63),
                onClick = onLogoutRequest
            )
        }
    }
}

@Composable
private fun ProfileButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = Color.White)
        }
    }
}

@Composable
private fun ProfileInfoSection(user: User, dateFormatter: SimpleDateFormat) {
    // Nombre de usuario
    Text(
        text = user.username,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Sección de información personal
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            ProfileInfoItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = user.email
            )

            ProfileInfoItem(
                icon = Icons.Default.CalendarToday,
                label = "Fecha de nacimiento",
                value = dateFormatter.format(user.birthDate)
            )

            ProfileInfoItem(
                icon = Icons.Default.Home,
                label = "Dirección",
                value = user.address
            )

            ProfileInfoItem(
                icon = Icons.Default.Public,
                label = "País",
                value = user.country
            )

            ProfileInfoItem(
                icon = Icons.Default.Phone,
                label = "Teléfono",
                value = user.phoneNumber
            )

            // Preferencias de usuario
            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Recibir ofertas por email",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (user.acceptEmailsOffers) "Activado" else "Desactivado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (user.acceptEmailsOffers)
                            Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6200EE),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}