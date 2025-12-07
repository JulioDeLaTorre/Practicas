package com.example.practicas.Modelos

import Agent
import Drink
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicas.Api.CocktailRetrofitClient
import com.example.practicas.Api.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HubViewModel : ViewModel() {

    // 1. TODOS LOS AGENTES (Necesario para que el buscador de Favoritos funcione con cualquiera)
    private val _allAgents = MutableStateFlow<List<Agent>>(emptyList())
    val allAgents = _allAgents.asStateFlow()

    // 2. AGENTES META (Investigación aplicada: Tier S + A 2025)
    private val _metaAgents = MutableStateFlow<List<Agent>>(emptyList())
    val metaAgents = _metaAgents.asStateFlow()

    // 3. BEBIDAS FAVORITAS (Detalles completos cargados por ID)
    private val _favoriteDrinksData = MutableStateFlow<List<Drink>>(emptyList())
    val favoriteDrinksData = _favoriteDrinksData.asStateFlow()

    // 4. BEBIDAS SUGERIDAS (Populares)
    private val _popularDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val popularDrinks = _popularDrinks.asStateFlow()

    init {
        loadHubData()
    }

    // Refresca favoritos al volver a la pantalla
    fun refreshFavorites() {
        loadFavoriteDrinksDetails()
    }

    private fun loadHubData() {
        viewModelScope.launch {
            try {
                // A) CARGAR AGENTES VALORANT
                val agentsResponse = RetrofitClient.api.getAgents()
                val allFetchedAgents = agentsResponse.data

                // Guardamos TODOS para que FavoritesManager pueda encontrar a quien sea (ej. Deadlock)
                _allAgents.value = allFetchedAgents

                // FILTRO INTELIGENTE: Basado en el Meta actual (Jett, Clove, Gekko, etc.)
                // Estos nombres son los "God Tier" del parche actual.
                val metaNames = listOf(
                    "Raze","Omen","Jett","Skye","Sova","Cypher","Killjoy","Viper","Reyna","Chamber"
                )

                // Filtramos la lista completa buscando estos nombres
                _metaAgents.value = allFetchedAgents.filter { agent ->
                    metaNames.contains(agent.displayName)
                }

                // B) CARGAR COCTELES POPULARES
                // "Margarita" siempre trae buenos resultados visuales para el demo
                val popularCocktailNames = listOf(
                    "Margarita",
                    "Mojito",
                    "Old Fashioned",
                    "Negroni",
                    "Espresso Martini",
                    "Daiquiri",
                    "Whiskey Sour",
                    "Cosmopolitan"
                )

                // Hacemos peticiones en paralelo para buscar cada uno
                // Deferred<Drink?> significa que esperamos un trago o nulo
                val tasks = popularCocktailNames.map { name ->
                    async {
                        try {
                            // Buscamos por nombre y tomamos EL PRIMERO (el más relevante)
                            val response = CocktailRetrofitClient.api.searchCocktail(name)
                            response.drinks?.firstOrNull()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                // Esperamos a que todas terminen, filtramos nulos y actualizamos la lista
                // .awaitAll() devuelve la lista de resultados
                // .filterNotNull() quita los errores
                _popularDrinks.value = tasks.awaitAll().filterNotNull()

                loadFavoriteDrinksDetails()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Carga los detalles técnicos de los cocteles guardados en Favoritos
    private fun loadFavoriteDrinksDetails() {
        viewModelScope.launch {
            val favIds = FavoritesManager.favoriteCocktails

            // Si no hay favoritos, limpiamos y salimos
            if (favIds.isEmpty()) {
                _favoriteDrinksData.value = emptyList()
                return@launch
            }

            val loadedDrinks = mutableListOf<Drink>()

            // Recorremos cada ID guardado y pedimos sus detalles a la API
            favIds.forEach { id ->
                try {
                    val response = CocktailRetrofitClient.api.lookupCocktail(id)
                    response.drinks?.firstOrNull()?.let { loadedDrinks.add(it) }
                } catch (e: Exception) {
                    // Si falla uno, continuamos con los demás
                }
            }
            _favoriteDrinksData.value = loadedDrinks
        }
    }
}