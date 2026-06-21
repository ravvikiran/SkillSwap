package com.skillswap.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillswap.app.domain.model.User
import com.skillswap.app.domain.repository.AuthRepository
import com.skillswap.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onDisplayNameChange(name: String) {
        _uiState.update { it.copy(displayName = name, errorMessage = null) }
    }

    fun signInWithEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.signInWithEmail(_uiState.value.email, _uiState.value.password)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSignedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Sign in failed")
                    }
                }
            )
        }
    }

    fun signUpWithEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            val result = authRepository.signUpWithEmail(state.email, state.password, state.displayName)
            result.fold(
                onSuccess = { user ->
                    // Create user document in Firestore
                    userRepository.createUser(user)
                    _uiState.update { it.copy(isLoading = false, isSignedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Sign up failed")
                    }
                }
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = { user ->
                    // Check if user exists, if not create
                    val existingUser = userRepository.getUser(user.id).getOrNull()
                    if (existingUser == null) {
                        userRepository.createUser(user)
                    }
                    _uiState.update { it.copy(isLoading = false, isSignedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Google sign in failed")
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
