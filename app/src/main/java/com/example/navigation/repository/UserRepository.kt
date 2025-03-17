package com.example.navigation.repository

import com.example.navigation.modules.Trip
import com.example.navigation.modules.User
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() : UserInterface {

    // Simulación de una "base de datos" en memoria
    private val UserList = mutableListOf<User>()

    override fun register(
        userId: String,
        name: String,
        email: String,
        phoneNumber: String,
        dateOfBirth: Date,
        passwordHash: String
    ) {
        // Crear el objeto Trip, usando listas vacías para itinerario y fotos
        val newUser = User(userId, name, email, phoneNumber, dateOfBirth, passwordHash)

        // Simular la persistencia: agregar el nuevo viaje a la "base de datos" en memoria
        val isAdded = UserList.add(newUser)

        if (!isAdded){
            throw IllegalArgumentException("No se ha podido añadir el Usuario: $name")
        }
    }

    override fun updateProfile(
        userId: String,
        name: String,
        email: String,
        phoneNumber: String,
        dateOfBirth: Date,
        passwordHash: String
    ) {
        val UserToUpdate = UserList.find { it.userId == userId }
        UserToUpdate?.let {
            it.name = name
            it.email = email
            it.phoneNumber = phoneNumber
            it.dateOfBirth = dateOfBirth
            it.passwordHash = passwordHash
            // Aquí podrías actualizar otros campos, si fuera necesario.
        }
    }

    override fun deleteAccount(userId: String) {
        val removed = UserList.removeIf { it.userId == userId }
        if (!removed) {
            throw IllegalArgumentException("No se encontró un Usuario con userId: $userId")
        }
    }
}