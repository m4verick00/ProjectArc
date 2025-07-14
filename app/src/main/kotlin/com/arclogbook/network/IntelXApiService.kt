package com.arclogbook.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

/**
 * IntelX API integration for OSINT (requires API key).
 * See: https://github.com/IntelligenceX/SDK
 */
interface IntelXApiService {
    @GET("/intelligence/search")
    suspend fun searchIntelX(
        @Query("term") term: String,
        @Query("maxresults") maxResults: Int = 10,
        @Query("media") media: Int = 0,
        @Query("apikey") apiKey: String
    ): Response<Any>
}
