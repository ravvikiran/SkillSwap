package com.skillswap.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillswap.app.domain.model.User
import com.skillswap.app.domain.repository.AuthRepository
import com.skillswap.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val userId = authRepository.currentUserId
        if (userId == null) {
            _uiState.update { it.copy(isLoading = false, user = null) }
            return
        }

        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            userRepository.observeUser(userId)
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
                .collect { user ->
                    _uiState.update {
                        it.copy(isLoading = false, user = user)
                    }
                }
        }
    }

    fun signOut(onComplete: () -> Unit) {
        observeJob?.cancel()
        viewModelScope.launch {
            authRepository.signOut()
            onComplete()
        }
    }
}
