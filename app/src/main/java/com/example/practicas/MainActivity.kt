package com.example.practicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.practicas.Navegacion.NavManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.practicas.Modelos.FavoritesManager.init(this)
        setContent {
            MaterialTheme {
                NavManager()
            }
        }
    }
}