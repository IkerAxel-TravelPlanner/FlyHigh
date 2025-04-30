package com.example.FlyHigh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FlyHigh.domain.model.User
import com.example.FlyHigh.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    private val auth = FirebaseAuth.getInstance()

    fun loadUserData(userId: Long?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val targetId = userId ?: getUserIdFromFirebase()

                if (targetId != null) {
                    userRepository.getUserById(targetId).collect { user ->
                        if (user != null) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    userData = user,
                                    username = user.username,
                                    email = user.email,
                                    birthDate = user.birthDate,
                                    address = user.address,
                                    country = user.country,
                                    phoneNumber = user.phoneNumber,
                                    acceptEmailsOffers = user.acceptEmailsOffers
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No se encontró el usuario"
                                )
                            }
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo determinar el ID del usuario"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el perfil: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private suspend fun getUserIdFromFirebase(): Long? {
        val firebaseUser = auth.currentUser ?: return null
        val user = userRepository.getUserByFirebaseUid(firebaseUser.uid)
        return user?.id
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    // Email ahora es de solo lectura - este método no debería tener efecto real
    fun updateEmail(email: String) {
        // No actualizamos el email en el uiState, esta función queda para mantener
        // compatibilidad con la interfaz pero no hace nada
    }

    fun updateBirthDate(birthDate: Date) {
        _uiState.update { it.copy(birthDate = birthDate) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun updateCountry(country: String) {
        _uiState.update { it.copy(country = country) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateAcceptEmailsOffers(accept: Boolean) {
        _uiState.update { it.copy(acceptEmailsOffers = accept) }
    }

    fun validateInputs(): Boolean {
        val currentState = _uiState.value

        val usernameValid = currentState.username.isNotBlank() && currentState.username.length >= 3
        val phoneValid = currentState.phoneNumber.isNotBlank() && currentState.phoneNumber.length >= 9
        // Ya no validamos el email porque ahora es de solo lectura

        return usernameValid && phoneValid
    }

    fun getInputErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val currentState = _uiState.value

        if (currentState.username.isBlank()) {
            errors["username"] = "El nombre de usuario no puede estar vacío"
        } else if (currentState.username.length < 3) {
            errors["username"] = "El nombre de usuario debe tener al menos 3 caracteres"
        }

        if (currentState.phoneNumber.isBlank()) {
            errors["phone"] = "El número de teléfono no puede estar vacío"
        } else if (currentState.phoneNumber.length < 9) {
            errors["phone"] = "El número de teléfono debe tener al menos 9 dígitos"
        }

        return errors
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val originalUser = currentState.userData

        if (originalUser == null) {
            _uiState.update { it.copy(error = "No se pudo obtener la información del usuario original") }
            return
        }

        if (!validateInputs()) {
            _uiState.update { it.copy(error = "Por favor, corrija los errores en el formulario") }
            return
        }

        val updatedUser = originalUser.copy(
            username = currentState.username,
            // Mantenemos el email original, no lo actualizamos con el del estado
            email = originalUser.email,
            birthDate = currentState.birthDate,
            address = currentState.address,
            country = currentState.country,
            phoneNumber = currentState.phoneNumber,
            acceptEmailsOffers = currentState.acceptEmailsOffers
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                userRepository.updateUser(updatedUser)
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al guardar los cambios: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val userData: User? = null,
    val username: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val address: String = "",
    val country: String = "",
    val phoneNumber: String = "",
    val acceptEmailsOffers: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)