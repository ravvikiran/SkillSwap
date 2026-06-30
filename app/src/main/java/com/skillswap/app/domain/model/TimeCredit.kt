package com.skillswap.app.domain.model

import androidx.annotation.Keep

@Keep
data class TimeCredit(
    val id: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val type: TransactionType = TransactionType.EARNED,
    val relatedUserId: String = "",
    val relatedUserName: String = "",
    val skillName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    EARNED,
    SPENT,
    BONUS
}
