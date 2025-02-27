package com.example.navigation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.navigation.ui.theme.NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Splash Screen logic
        var isChecking = true
        lifecycleScope.launch {
            delay(3000L)  // Splash duration
            isChecking = false
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition { isChecking }
        }

        // Set content for the MainActivity
        setContent {
            NavigationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen()  // Show the main screen content
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current  // Get the context in Compose
    Column(modifier = Modifier.padding(16.dp)) {
        // Button to open Terms and Conditions Activity
        Button(onClick = {
            val intent = Intent(context, TermsConditionsActivity::class.java)
            context.startActivity(intent)  // Start the new activity
        }) {
            Text(text = "Open Terms and Conditions")
        }

        // Button to open About Activity
        Button(onClick = {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)  // Start AboutActivity
        }) {
            Text(text = "Open About")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigationTheme {
        MainScreen()  // Preview the main screen layout
    }
}
