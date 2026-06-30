package com.skillswap.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillswap.app.domain.model.LatLng
import com.skillswap.app.domain.model.Skill
import com.skillswap.app.domain.model.SkillCategory
import com.skillswap.app.domain.repository.AuthRepository
import com.skillswap.app.domain.repository.LocationRepository
import com.skillswap.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class LocationUiState(
    val isLoading: Boolean = false,
    val isDetecting: Boolean = false,
    val location: LatLng? = null,
    val neighborhood: String = "",
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val isLocationSet: Boolean = false
)

data class SkillsUiState(
    val isLoading: Boolean = false,
    val skillsOffered: List<Skill> = emptyList(),
    val skillsNeeded: List<Skill> = emptyList(),
    val currentSkillName: String = "",
    val currentSkillCategory: SkillCategory = SkillCategory.OTHER,
    val currentSkillDescription: String = "",
    val isAddingOffer: Boolean = true, // true = offer, false = need
    val showAddDialog: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _locationState = MutableStateFlow(LocationUiState())
    val locationState: StateFlow<LocationUiState> = _locationState.asStateFlow()

    private val _skillsState = MutableStateFlow(SkillsUiState())
    val skillsState: StateFlow<SkillsUiState> = _skillsState.asStateFlow()

    fun detectLocation() {
        viewModelScope.launch {
            _locationState.update { it.copy(isDetecting = true, errorMessage = null) }
            val locationResult = locationRepository.getCurrentLocation()
            locationResult.fold(
                onSuccess = { geoPoint ->
                    val nameResult = locationRepository.getNeighborhoodName(geoPoint)
                    val neighborhood = nameResult.getOrDefault("Your Neighborhood")
                    _locationState.update {
                        it.copy(
                            isDetecting = false,
                            location = geoPoint,
                            neighborhood = neighborhood,
                            isLocationSet = true
                        )
                    }
                },
                onFailure = { error ->
                    _locationState.update {
                        it.copy(
                            isDetecting = false,
                            errorMessage = "Could not detect location. Try searching manually."
                        )
                    }
                }
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _locationState.update { it.copy(searchQuery = query) }
    }

    fun searchAndSetLocation() {
        viewModelScope.launch {
            val query = _locationState.value.searchQuery
            if (query.isBlank()) return@launch

            _locationState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = locationRepository.searchLocation(query)
            result.fold(
                onSuccess = { results ->
                    val first = results.firstOrNull()
                    if (first != null) {
                        _locationState.update {
                            it.copy(
                                isLoading = false,
                                location = first.location,
                                neighborhood = first.name,
                                isLocationSet = true
                            )
                        }
                    } else {
                        _locationState.update {
                            it.copy(isLoading = false, errorMessage = "No results found")
                        }
                    }
                },
                onFailure = { error ->
                    _locationState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            )
        }
    }

    fun saveLocation() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId ?: return@launch
            val state = _locationState.value
            val location = state.location ?: return@launch

            _locationState.update { it.copy(isLoading = true) }
            userRepository.updateLocation(userId, location, state.neighborhood)
            _locationState.update { it.copy(isLoading = false) }
        }
    }

    // Skills management
    fun showAddSkillDialog(isOffer: Boolean) {
        _skillsState.update {
            it.copy(
                showAddDialog = true,
                isAddingOffer = isOffer,
                currentSkillName = "",
                currentSkillCategory = SkillCategory.OTHER,
                currentSkillDescription = ""
            )
        }
    }

    fun dismissAddDialog() {
        _skillsState.update { it.copy(showAddDialog = false) }
    }

    fun onSkillNameChange(name: String) {
        _skillsState.update { it.copy(currentSkillName = name) }
    }

    fun onSkillCategoryChange(category: SkillCategory) {
        _skillsState.update { it.copy(currentSkillCategory = category) }
    }

    fun onSkillDescriptionChange(desc: String) {
        _skillsState.update { it.copy(currentSkillDescription = desc) }
    }

    fun addSkill() {
        val state = _skillsState.value
        if (state.currentSkillName.isBlank()) return

        val skill = Skill(
            id = UUID.randomUUID().toString(),
            name = state.currentSkillName.trim(),
            category = state.currentSkillCategory,
            description = state.currentSkillDescription.trim()
        )

        _skillsState.update {
            if (state.isAddingOffer) {
                it.copy(
                    skillsOffered = it.skillsOffered + skill,
                    showAddDialog = false
                )
            } else {
                it.copy(
                    skillsNeeded = it.skillsNeeded + skill,
                    showAddDialog = false
                )
            }
        }
    }

    fun removeOfferedSkill(skill: Skill) {
        _skillsState.update {
            it.copy(skillsOffered = it.skillsOffered - skill)
        }
    }

    fun removeNeededSkill(skill: Skill) {
        _skillsState.update {
            it.copy(skillsNeeded = it.skillsNeeded - skill)
        }
    }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId ?: return@launch
            _skillsState.update { it.copy(isLoading = true) }

            val state = _skillsState.value
            userRepository.updateSkillsOffered(userId, state.skillsOffered)
            userRepository.updateSkillsNeeded(userId, state.skillsNeeded)
            userRepository.completeOnboarding(userId)

            _skillsState.update { it.copy(isLoading = false) }
            onComplete()
        }
    }
}
