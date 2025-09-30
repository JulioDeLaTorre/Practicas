package com.example.practicas.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.practicas.R
import com.example.practicas.components.TeamInfo
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Kansas(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Kansas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE31837)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("AFC")
                    }
                })

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TeamInfo(
                R.drawable.kansase, "Fundación: 1960 (Dallas Texans) | Ciudad: Kansas City, MO\n" +
                        "Super Bowls Ganados: 3 (1969, 2019, 2023)\n" +
                        "Jugadores icónicos: Patrick Mahomes, Travis Kelce\n" +
                        "Curiosidad: Dinastía actual de la NFL, ataque explosivo", Color(0xFFE31837),
                        Color.Black
            )
        }
    }
}