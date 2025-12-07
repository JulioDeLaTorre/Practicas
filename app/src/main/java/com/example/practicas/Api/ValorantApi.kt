package com.example.practicas.Api // Ajusta a tu paquete

import ValorantResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ValorantApiService {
    // Quitamos el parametro de idioma
    @GET("v1/agents")
    suspend fun getAgents(
        @Query("isPlayableCharacter") isPlayable: Boolean = true
    ): ValorantResponse
}

object RetrofitClient {
    // MANTÉN ESTO: Es vital para que no falle con los datos extra que trae el inglés
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val api: ValorantApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://valorant-api.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ValorantApiService::class.java)
    }
}