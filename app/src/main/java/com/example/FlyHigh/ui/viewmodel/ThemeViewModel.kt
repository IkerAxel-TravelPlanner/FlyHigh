package com.example.FlyHigh.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    // El estado del tema oscuro o claro
    var isDarkTheme = mutableStateOf(false) // Comienza en modo claro

    fun toggleTheme() {
        // Cambia entre tema claro y oscuro
        isDarkTheme.value = !isDarkTheme.value
    }
}
