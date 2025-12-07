package com.example.practicas.Modelos

import Drink
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicas.Api.CocktailRetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LicoreriaViewModel : ViewModel() {

    // ... (Tus estados anteriores searchResults, families, selectedDrink) ...
    private val _searchResults = MutableStateFlow<List<Drink>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _families = MutableStateFlow<Map<String, List<Drink>>>(emptyMap())
    val families = _families.asStateFlow()

    private val _selectedDrink = MutableStateFlow<Drink?>(null)
    val selectedDrink = _selectedDrink.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // NUEVO: Estado para navegación de un solo tiro (Random)
    private val _randomDrinkId = MutableStateFlow<String?>(null)
    val randomDrinkId = _randomDrinkId.asStateFlow()

    init {
        loadDefaultFamilies()
    }

    // --- FUNCIÓN INTELIGENTE: NOMBRE O INGREDIENTE ---
    fun searchCocktail(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
                return@launch
            }

            _isLoading.value = true
            try {
                // 1. Intento buscar por NOMBRE (ej. "Margarita")
                val nameResponse = CocktailRetrofitClient.api.searchCocktail(query)

                if (!nameResponse.drinks.isNullOrEmpty()) {
                    // ¡Éxito! Encontramos por nombre
                    _searchResults.value = nameResponse.drinks
                } else {
                    // 2. Falló nombre, intento por INGREDIENTE (ej. "Vodka")
                    val ingredientResponse = CocktailRetrofitClient.api.filterByIngredient(query)
                    _searchResults.value = ingredientResponse.drinks ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FUNCIÓN RANDOM ---
    fun findRandomCocktail() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = CocktailRetrofitClient.api.getRandomCocktail()
                val randomId = response.drinks?.firstOrNull()?.idDrink
                _randomDrinkId.value = randomId // Esto disparará la navegación en la UI
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpia el evento de navegación random para no rebotar
    fun clearRandomNavigation() {
        _randomDrinkId.value = null
    }

    // ... (Resto de funciones loadDefaultFamilies y loadDrinkDetail IGUAL QUE ANTES) ...
    private fun loadDefaultFamilies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val familyNames = listOf("Margarita", "Mojito", "Daiquiri", "Paloma")
                val tasks = familyNames.map { name ->
                    async { name to (CocktailRetrofitClient.api.searchCocktail(name).drinks ?: emptyList()) }
                }
                _families.value = tasks.awaitAll().toMap()
            } catch (e: Exception) { e.printStackTrace() } finally { _isLoading.value = false }
        }
    }

    fun loadDrinkDetail(id: String) {
        viewModelScope.launch {
            _selectedDrink.value = null
            _isLoading.value = true
            try {
                val response = CocktailRetrofitClient.api.lookupCocktail(id)
                _selectedDrink.value = response.drinks?.firstOrNull()
            } catch (e: Exception) { e.printStackTrace() } finally { _isLoading.value = false }
        }
    }
}