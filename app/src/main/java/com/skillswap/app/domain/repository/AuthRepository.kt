package com.skillswap.app.domain.repository

import com.skillswap.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: String?
    val isAuthenticated: Boolean
    fun observeAuthState(): Flow<Boolean>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, displayName: String): Result<User>
    suspend fun signOut()
}
