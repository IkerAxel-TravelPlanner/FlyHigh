package com.example.FlyHigh.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen2(navController: NavController) {

    // States para el nombre de usuario, contraseña y el diálogo de alerta
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    // Obtener los valores predeterminados desde strings.xml
    val defaultUser = stringResource(id = R.string.default_user)
    val defaultPass = stringResource(id = R.string.default_pass)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),  // Mayor padding
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF6200EE)  // Color morado
        )

        // Campo de texto para el nombre de usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username)) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Campo de texto para la contraseña
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

        // Botón de login con estilo atractivo
        ElevatedButton(
            onClick = {
                if (username == defaultUser && password == defaultPass) {
                    // Navegar a la pantalla Home y eliminar Login de la pila de navegación
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    showAlert = true
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color consistente
        ) {
            Text(text = stringResource(id = R.string.login_button), color = Color.White)
        }
    }

    // Mostrar el cuadro de diálogo de alerta si el login falla
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text(stringResource(id = R.string.login_failed_title)) },
            text = { Text(stringResource(id = R.string.login_failed_message)) },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text(stringResource(id = R.string.ok_button))
                }
            }
        )
    }
}
