package com.arclogbook.osint

import retrofit2.http.GET
import retrofit2.http.Query

interface AbuseIpdbApi {
    @GET("check")
    suspend fun checkIp(@Query("ipAddress") ip: String): Any // Replace Any with actual model
}
