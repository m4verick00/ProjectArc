package com.arclogbook.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

/**
 * AlienVault OTX API integration for threat intelligence (requires API key).
 * See: https://otx.alienvault.com/api/
 */
interface OtxApiService {
    @GET("/api/v1/indicators/domain/{domain}/general")
    suspend fun getDomainInfo(
        @Path("domain") domain: String,
        @Query("apikey") apiKey: String
    ): Response<Any>
}
