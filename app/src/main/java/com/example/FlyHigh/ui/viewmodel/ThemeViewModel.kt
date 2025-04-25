package com.example.FlyHigh.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {
    // El estado del tema oscuro o claro
    var isDarkTheme = mutableStateOf(false) // Comienza en modo claro

    fun toggleTheme() {
        // Cambia entre tema claro y oscuro
        isDarkTheme.value = !isDarkTheme.value
    }
}