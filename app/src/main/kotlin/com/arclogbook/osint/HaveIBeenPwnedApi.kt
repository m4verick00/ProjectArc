package com.arclogbook.osint

import retrofit2.http.GET
import retrofit2.http.Path

interface HaveIBeenPwnedApi {
    @GET("breachedaccount/{email}")
    suspend fun getBreaches(@Path("email") email: String): List<Any> // Replace Any with actual model
}
