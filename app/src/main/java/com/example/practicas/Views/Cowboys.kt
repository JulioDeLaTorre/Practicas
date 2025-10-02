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
fun Cowboys(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Cowboys") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF869397)
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
                R.drawable.cowboyse,
                "Fundación: 1960 | Ciudad: Arlington, TX\n" +
                        "Super Bowls Ganados: 5 (1971, 1977, 1992, 1993, 1995)\n" +
                        "Jugadores icónicos: Roger Staubach, Emmitt Smith, Troy Aikman\n",
                Color(0xFF869397)
            )}
            item{LottieCard(R.raw.redfootballrotating)}
            item{Curiosidades("Curiosidades:\nApodados 'America’s Team' por su popularidad nacional." +
                    "Son el equipo más valioso de la NFL según Forbes." +
                    "Juegan en el AT&T Stadium, uno de los estadios más grandes y modernos del mundo.",Color(0xFF869397)) }
        }
    }
}