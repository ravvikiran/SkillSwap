package com.skillswap.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.skillswap.app.domain.model.LatLng
import com.skillswap.app.domain.repository.LocationRepository
import com.skillswap.app.domain.repository.LocationSearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<LatLng> {
        return try {
            val cancellationTokenSource = CancellationTokenSource()
            try {
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).await()
                if (location != null) {
                    Result.success(LatLng(location.latitude, location.longitude))
                } else {
                    Result.failure(Exception("Unable to get current location"))
                }
            } finally {
                cancellationTokenSource.cancel()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("DEPRECATION")
    override suspend fun getNeighborhoodName(location: LatLng): Result<String> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            )
            val neighborhood = addresses?.firstOrNull()?.let { address ->
                address.subLocality ?: address.locality ?: address.subAdminArea ?: "Unknown"
            } ?: "Unknown"
            Result.success(neighborhood)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("DEPRECATION")
    override suspend fun searchLocation(query: String): Result<List<LocationSearchResult>> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(query, 5) ?: emptyList()
            val results = addresses.map { address ->
                LocationSearchResult(
                    name = address.featureName ?: address.locality ?: query,
                    address = address.getAddressLine(0) ?: "",
                    location = LatLng(address.latitude, address.longitude)
                )
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
