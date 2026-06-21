package com.skillswap.app.domain.repository

import com.skillswap.app.domain.model.GeoPoint

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<GeoPoint>
    suspend fun getNeighborhoodName(location: GeoPoint): Result<String>
    suspend fun searchLocation(query: String): Result<List<LocationSearchResult>>
}

data class LocationSearchResult(
    val name: String,
    val address: String,
    val location: GeoPoint
)
