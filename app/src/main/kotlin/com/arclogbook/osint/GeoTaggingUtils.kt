package com.arclogbook.osint

import android.location.Geocoder
import android.content.Context

object GeoTaggingUtils {
    fun getLocationName(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses?.firstOrNull()?.getAddressLine(0)
    }
}
