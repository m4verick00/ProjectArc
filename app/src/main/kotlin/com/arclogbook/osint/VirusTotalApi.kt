package com.arclogbook.osint

import retrofit2.http.GET
import retrofit2.http.Query

interface VirusTotalApi {
    @GET("files/search")
    suspend fun searchFiles(@Query("query") query: String): Any // Replace Any with actual model
}
