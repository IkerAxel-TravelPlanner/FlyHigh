package com.example.FlyHigh.ui.view

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.FlyHigh.R
import com.example.LowTravel.utils.updateLocaleAndRecreate
import com.example.LowTravel.data.SharedPrefsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(navController: NavController, context: Context) {
    val sharedPrefsManager = remember {
        SharedPrefsManager(context.getSharedPreferences("LowTravel_preferences", Context.MODE_PRIVATE))
    }

    var selectedLanguage by remember { mutableStateOf(sharedPrefsManager.userLanguage ?: "en") }

    val primaryColor = Color(0xFF6200EE) // Color morado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.change_language)) },
                navigationIcon = {
                    // Icono de retroceso
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = primaryColor)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.change_language),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Lista de idiomas
                val languages = listOf(
                    "en" to stringResource(id = R.string.english),
                    "es" to stringResource(id = R.string.spanish),
                    "ca" to stringResource(id = R.string.catalan)
                )

                // Crear los botones para cada idioma
                languages.forEach { (code, label) ->
                    ElevatedButton(
                        onClick = {
                            selectedLanguage = code
                            sharedPrefsManager.userLanguage = code
                            updateLocaleAndRecreate(context as Activity, code) // Reiniciar app con nuevo idioma
                        },
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(label, color = Color.White)
                    }
                }
            }
        }
    )
}
