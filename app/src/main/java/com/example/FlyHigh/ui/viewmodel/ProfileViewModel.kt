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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val auth = FirebaseAuth.getInstance()

    fun loadUserData(userId: Long?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val targetId = userId ?: getUserIdFromFirebase()

                if (targetId != null) {
                    // Observar cambios en el usuario
                    userRepository.getUserById(targetId).collect { user ->
                        if (user != null) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    userData = user,
                                    isCurrentUserProfile = isCurrentUserProfile(user)
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

    private fun isCurrentUserProfile(user: User): Boolean {
        val currentFirebaseUser = auth.currentUser ?: return false
        return user.firebaseUid == currentFirebaseUser.uid
    }

    fun logout() {
        auth.signOut()
        _uiState.update { ProfileUiState() }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userData: User? = null,
    val error: String? = null,
    val isCurrentUserProfile: Boolean = false
)