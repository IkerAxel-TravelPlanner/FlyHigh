package com.example.FlyHigh.utils

import java.util.regex.Pattern

object FormValidationUtils {

    fun validateUserEmail(email: String): Boolean {
        return email.isNotEmpty()
                //&& isEmailValid(email)
    }

    fun validatePassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }

    fun validateConfirmationPassword(
        password: String,
        confirmationPassword: String
    ): Boolean {
        return confirmationPassword.isNotEmpty() && confirmationPassword == password
    }

    fun validateStoreName(storeName: String): Boolean {
        return storeName.isNotEmpty()
    }

    fun validateStoreLocation(storeLoc: String): Boolean {
        return storeLoc.isNotEmpty()
    }

    fun validateMobile(mobile: String): Boolean {
        return mobile.isNotEmpty() && mobile.length == 10
    }

    fun validatePin(pinCode: String): Boolean {
        return pinCode.isNotEmpty() && pinCode.length == 6
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#\$%^&*+=])(?=\\S+\$).{8,}$"
        return Pattern.compile(passwordPattern).matcher(password).matches()
    }
}