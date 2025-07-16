package com.arclogbook.osint

import retrofit2.http.GET
import retrofit2.http.Query

interface ShodanApi {
    @GET("shodan/host/search")
    suspend fun searchHosts(@Query("query") query: String): Any // Replace Any with actual model
}
