package com.example.FlyHigh.domain.model

class Authentication {
    fun login(email: String, password: String) {}
    fun logout() {}
    fun resetPassword(email: String) {}
    fun isAuthenticated(): Boolean = false
    fun twoFactorAuth() {}
}