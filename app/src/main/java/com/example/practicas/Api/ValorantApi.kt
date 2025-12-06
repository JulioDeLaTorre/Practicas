import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

interface ValorantApiService {
    @GET("v1/agents")
    suspend fun getAgents(
        @Query("isPlayableCharacter") isPlayable: Boolean = true, // Filtramos cosas raras que no son jugables
        @Query("language") language: String = "es-MX" // Opcional: pedir datos en español
    ): ValorantResponse
}

object RetrofitClient {
    private val json = Json { ignoreUnknownKeys = true } // Ignora campos que no mapeamos

    val api: ValorantApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://valorant-api.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ValorantApiService::class.java)
    }
}