package com.skillswap.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.skillswap.app.domain.model.GeoPoint
import com.skillswap.app.domain.model.Skill
import com.skillswap.app.domain.model.User
import com.skillswap.app.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override fun observeUser(userId: String): Flow<User?> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)
                trySend(user)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getUser(userId: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
                ?: throw Exception("User not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.id).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.id).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateLocation(
        userId: String,
        location: GeoPoint,
        neighborhood: String
    ): Result<Unit> {
        return try {
            usersCollection.document(userId).update(
                mapOf(
                    "location" to location,
                    "neighborhood" to neighborhood
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSkillsOffered(userId: String, skills: List<Skill>): Result<Unit> {
        return try {
            usersCollection.document(userId).update("skillsOffered", skills).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSkillsNeeded(userId: String, skills: List<Skill>): Result<Unit> {
        return try {
            usersCollection.document(userId).update("skillsNeeded", skills).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeOnboarding(userId: String): Result<Unit> {
        return try {
            usersCollection.document(userId).update("isOnboardingComplete", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNearbyUsers(location: GeoPoint, radiusKm: Double): Result<List<User>> {
        return try {
            // Simple distance-based query (for MVP; consider GeoFire for production)
            val allUsers = usersCollection.get().await()
            val nearbyUsers = allUsers.documents
                .mapNotNull { it.toObject(User::class.java) }
                .filter { user ->
                    user.location?.let { loc ->
                        calculateDistance(
                            location.latitude, location.longitude,
                            loc.latitude, loc.longitude
                        ) <= radiusKm
                    } ?: false
                }
            Result.success(nearbyUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }
}
