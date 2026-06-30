package com.skillswap.app.domain.model

import androidx.annotation.Keep

@Keep
data class User(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val neighborhood: String = "",
    val location: LatLng? = null,
    val skillsOffered: List<Skill> = emptyList(),
    val skillsNeeded: List<Skill> = emptyList(),
    val bio: String = "",
    val trustScore: Float = 0f,
    val totalReviews: Int = 0,
    val timeCredits: Int = 3, // Everyone starts with 3 free credits
    val createdAt: Long = System.currentTimeMillis(),
    val isOnboardingComplete: Boolean = false
)

@Keep
data class LatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Keep
data class Skill(
    val id: String = "",
    val name: String = "",
    val category: SkillCategory = SkillCategory.OTHER,
    val description: String = ""
)

enum class SkillCategory(val displayName: String) {
    TECHNOLOGY("Technology"),
    LANGUAGES("Languages"),
    MUSIC("Music"),
    COOKING("Cooking"),
    FITNESS("Fitness"),
    ARTS("Arts & Crafts"),
    HOME_REPAIR("Home Repair"),
    GARDENING("Gardening"),
    TUTORING("Tutoring"),
    PHOTOGRAPHY("Photography"),
    PET_CARE("Pet Care"),
    TRANSPORTATION("Transportation"),
    OTHER("Other")
}
