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
fun GreenBay(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Green Bay") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFB612)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("NFC")
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
                R.drawable.grenbaye,
                "Fundación: 1919 | Ciudad: Green Bay, WI\n" +
                        "Super Bowls Ganados: 4 (1966, 1967, 1996, 2010)\n" +
                        "Campeonatos de liga pre-Super Bowl: 9\n" +
                        "Jugadores icónicos: Brett Favre, Aaron Rodgers\n",
                Color(0xFFFFB612),Color.White
            )}
            item{ LottieCard(R.raw.chrisevans) }
            item{ Curiosidades("Curiosidades:\nSon el único equipo propiedad de los aficionados (con acciones públicas),\n" +
                    "Su estadio Lambeau Field es famoso por el 'Lambeau Leap',\n" +
                    "Fundados en 1919, son el tercer equipo más antiguo de la NFL.",Color(0xFFFFB612)) }
        }
    }
}