package com.skillswap.app.domain.model

import androidx.annotation.Keep

@Keep
data class SkillListing(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String? = null,
    val userTrustScore: Float = 0f,
    val skill: Skill = Skill(),
    val type: ListingType = ListingType.OFFER,
    val description: String = "",
    val location: LatLng? = null,
    val neighborhood: String = "",
    val distanceKm: Float? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ListingType {
    OFFER,
    NEED
}
