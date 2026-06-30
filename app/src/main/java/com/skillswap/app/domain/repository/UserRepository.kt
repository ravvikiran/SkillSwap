package com.skillswap.app.domain.repository

import com.skillswap.app.domain.model.LatLng
import com.skillswap.app.domain.model.Skill
import com.skillswap.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(userId: String): Flow<User?>
    suspend fun getUser(userId: String): Result<User>
    suspend fun createUser(user: User): Result<Unit>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun updateLocation(userId: String, location: LatLng, neighborhood: String): Result<Unit>
    suspend fun updateSkillsOffered(userId: String, skills: List<Skill>): Result<Unit>
    suspend fun updateSkillsNeeded(userId: String, skills: List<Skill>): Result<Unit>
    suspend fun completeOnboarding(userId: String): Result<Unit>
    suspend fun getNearbyUsers(location: LatLng, radiusKm: Double): Result<List<User>>
}
