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
import com.airbnb.lottie.compose.LottieAnimation
import com.example.practicas.R
import com.example.practicas.components.Curiosidades
import com.example.practicas.components.LottieCard
import com.example.practicas.components.TeamInfo
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Eagles(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Eagles") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFA5ACAF)
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
                R.drawable.eaglese,
                "Fundación: 1933 | Ciudad: Philadelphia, PA\n" +
                        "Super Bowls Ganados: 1 (2017)\n" +
                        "Jugadores icónicos: Donovan McNabb, Carson Wentz\n",
                Color(0xFFA5ACAF),Color.White
            )}
            item{LottieCard(R.raw.redfootballrotating)}
            item{ Curiosidades("Curiosidades:\nLa fanaticada es famosa por ser apasionada y exigente,\n" +
                    "Su grito 'Fly, Eagles, Fly' es un himno en los partidos,\n" +
                    "Ganaron su primer Super Bowl en 2018 contra los Patriots.",Color(0xFFA5ACAF))}
        }
    }
}