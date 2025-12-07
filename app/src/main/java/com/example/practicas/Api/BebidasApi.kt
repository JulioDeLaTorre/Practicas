package com.example.practicas.Api

import CocktailResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService {
    @GET("api/json/v1/1/search.php")
    suspend fun searchCocktail(@Query("s") query: String): CocktailResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun lookupCocktail(@Query("i") id: String): CocktailResponse

    // --- NUEVOS ENDPOINTS ---

    // 1. Coctel Aleatorio
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomCocktail(): CocktailResponse

    // 2. Filtrar por Ingrediente (ej. Vodka)
    @GET("api/json/v1/1/filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): CocktailResponse
}

object CocktailRetrofitClient {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    val api: CocktailApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CocktailApiService::class.java)
    }
}

