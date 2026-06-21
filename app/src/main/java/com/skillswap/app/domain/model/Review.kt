package com.skillswap.app.domain.model

data class Review(
    val id: String = "",
    val fromUserId: String = "",
    val fromUserName: String = "",
    val fromUserPhotoUrl: String? = null,
    val toUserId: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val skillName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
