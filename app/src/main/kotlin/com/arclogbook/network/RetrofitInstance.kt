package com.arclogbook.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val certificatePinner = CertificatePinner.Builder()
        .add("api.osint.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") // Replace with actual pin
        .build()
    private val client = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .build()
    val api: OsintApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.osint.example.com/") // Replace with actual base URL as needed
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OsintApiService::class.java)
    }
}
