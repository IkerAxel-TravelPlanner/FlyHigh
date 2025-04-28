package com.example.FlyHigh.utils

import android.util.Patterns
import java.util.regex.Pattern

object FormValidationsUtils {

    /**
     * Valida si un email tiene formato correcto
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Valida si una contraseña cumple con requisitos mínimos de seguridad
     * - Al menos 6 caracteres
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Verifica si las contraseñas coinciden
     */
    fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}