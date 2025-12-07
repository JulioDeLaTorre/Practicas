package com.example.practicas.Modelos

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

object FavoritesManager {
    private const val PREFS_NAME = "app_favorites"
    private const val KEY_AGENTS = "fav_agents"
    private const val KEY_COCKTAILS = "fav_cocktails"

    // Listas observables para la UI
    val favoriteAgents = mutableStateListOf<String>()
    val favoriteCocktails = mutableStateListOf<String>()

    private lateinit var prefs: SharedPreferences

    // ESTA FUNCIÓN ES LA CLAVE: Inicia la memoria del teléfono
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadFavorites()
    }

    private fun loadFavorites() {
        // Leemos del disco. Si no hay nada, devolvemos lista vacía.
        val savedAgents = prefs.getStringSet(KEY_AGENTS, emptySet()) ?: emptySet()
        val savedCocktails = prefs.getStringSet(KEY_COCKTAILS, emptySet()) ?: emptySet()

        favoriteAgents.clear()
        favoriteAgents.addAll(savedAgents)

        favoriteCocktails.clear()
        favoriteCocktails.addAll(savedCocktails)
    }

    private fun saveAgents() {
        // Guardamos en disco
        prefs.edit().putStringSet(KEY_AGENTS, favoriteAgents.toSet()).apply()
    }

    private fun saveCocktails() {
        // Guardamos en disco
        prefs.edit().putStringSet(KEY_COCKTAILS, favoriteCocktails.toSet()).apply()
    }

    // --- MÉTODOS PÚBLICOS (IGUAL QUE ANTES PERO AHORA GUARDAN) ---

    fun toggleAgentFavorite(uuid: String) {
        if (favoriteAgents.contains(uuid)) {
            favoriteAgents.remove(uuid)
        } else {
            favoriteAgents.add(uuid)
        }
        saveAgents() // <--- G U A R D A R
    }

    fun toggleCocktailFavorite(id: String) {
        if (favoriteCocktails.contains(id)) {
            favoriteCocktails.remove(id)
        } else {
            favoriteCocktails.add(id)
        }
        saveCocktails() // <--- G U A R D A R
    }

    fun isAgentFavorite(uuid: String): Boolean = favoriteAgents.contains(uuid)
    fun isCocktailFavorite(id: String): Boolean = favoriteCocktails.contains(id)
}