package com.arclogbook.osint

import retrofit2.http.GET
import retrofit2.http.Query

interface CensysApi {
    @GET("search/hosts")
    suspend fun searchHosts(@Query("q") query: String): Any // Replace Any with actual model
}
