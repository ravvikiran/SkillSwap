package com.skillswap.app.domain.repository

import com.skillswap.app.domain.model.LatLng

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<LatLng>
    suspend fun getNeighborhoodName(location: LatLng): Result<String>
    suspend fun searchLocation(query: String): Result<List<LocationSearchResult>>
}

data class LocationSearchResult(
    val name: String,
    val address: String,
    val location: LatLng
)
