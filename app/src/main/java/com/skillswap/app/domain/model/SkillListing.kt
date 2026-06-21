package com.skillswap.app.domain.model

data class SkillListing(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String? = null,
    val userTrustScore: Float = 0f,
    val skill: Skill = Skill(),
    val type: ListingType = ListingType.OFFER,
    val description: String = "",
    val location: GeoPoint? = null,
    val neighborhood: String = "",
    val distanceKm: Float? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ListingType {
    OFFER,
    NEED
}
