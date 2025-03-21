package com.example.FlyHigh.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    // Mantener el estado del tema claro/oscuro
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    // Cambiar el estado del tema
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}
