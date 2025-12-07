package com.example.practicas.Modelos

import Agent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicas.Api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents = _agents.asStateFlow()

    init {
        fetchAgents()
    }

    private fun fetchAgents() {
        viewModelScope.launch {
            try {
                // Llamada limpia, sin filtros de idioma
                val response = RetrofitClient.api.getAgents()

                // Si esto imprime "0", es que la API no da nada. Si imprime >20, funcionó.
                Log.d("API_SUCCESS", "Agentes cargados: ${response.data.size}")

                _agents.value = response.data
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }
    }
}