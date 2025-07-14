package com.arclogbook.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

/**
 * Real API integrations for OSINT sources. Only public and legal APIs are included here.
 * For sources without APIs, provide stubs and documentation for future implementation.
 */
interface OsintApiService {
    // HaveIBeenPwned (email breach check, requires API key)
    @GET("breachedaccount/{email}")
    suspend fun checkEmailBreach(
        @Path("email") email: String,
        @Query("truncateResponse") truncate: Boolean = true
    ): Response<List<Any>>

    // Shodan (public info, requires API key)
    @GET("shodan/host/{ip}")
    suspend fun shodanHostInfo(
        @Path("ip") ip: String,
        @Query("key") apiKey: String
    ): Response<Any>

    // CERT-In RSS (cyber alerts, public)
    @GET("feeds/cert-in.xml")
    suspend fun getCertInAlerts(): Response<String>

    // GitHub search (public)
    @GET("search/code")
    suspend fun searchGithubCode(
        @Query("q") query: String
    ): Response<Any>

    // Add more public APIs as needed
}
