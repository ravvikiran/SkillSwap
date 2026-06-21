package com.skillswap.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillswap.app.domain.repository.AuthRepository
import com.skillswap.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val isOnboardingComplete: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { isAuthenticated ->
                if (isAuthenticated) {
                    val userId = authRepository.currentUserId ?: return@collect
                    val userResult = userRepository.getUser(userId)
                    val user = userResult.getOrNull()
                    _authState.value = AuthState(
                        isLoading = false,
                        isAuthenticated = true,
                        isOnboardingComplete = user?.isOnboardingComplete ?: false
                    )
                } else {
                    _authState.value = AuthState(
                        isLoading = false,
                        isAuthenticated = false,
                        isOnboardingComplete = false
                    )
                }
            }
        }
    }
}
