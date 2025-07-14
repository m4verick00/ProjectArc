package com.arclogbook.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: OsintApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.osint.example.com/") // Replace with actual base URL as needed
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OsintApiService::class.java)
    }
}
