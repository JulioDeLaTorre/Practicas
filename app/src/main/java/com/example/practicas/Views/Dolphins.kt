package com.example.practicas.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.practicas.components.Curiosidades
import com.example.practicas.components.LottieCard
import com.example.practicas.components.TeamInfo
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Dolphins(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Dolphins") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFD1AA)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("AFC")
                    }
                })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item{TeamInfo(
                R.drawable.dolphinse,
                "Fundación: 1966 | Ciudad: Miami Gardens, FL\n" +
                        "Super Bowls Ganados: 2 (1972, 1973)\n" +
                        "Temporada perfecta: 1972 (17-0)\n" +
                        "Jugadores icónicos: Dan Marino\n",
                Color(0xFFFFD1AA),Color.White
            )}
            item{ LottieCard(R.raw.defaultlottie) }
            item{ Curiosidades("Curiosidades:\nEn 1972 lograron la única temporada perfecta en la historia de la NFL (17–0),\n" +
                    "El uniforme aqua-naranja es un ícono en la liga.,\n" +
                    "Don Shula, su coach legendario, es el más ganador de la NFL.",Color.White,Color(0xFFFFD1AA)) }
        }
    }
}
