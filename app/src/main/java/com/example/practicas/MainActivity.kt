package com.example.practicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.practicas.navigation.NavManager
import com.example.practicas.ui.theme.PracticasTheme
import com.example.practicas.viewModels.LoginViewModel
import com.example.practicas.viewModels.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginVM : LoginViewModel by viewModels()
        val notesVM : NotesViewModel by viewModels()
        setContent {
            PracticasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager(loginVM, notesVM)
                }
            }
        }
    }
}
