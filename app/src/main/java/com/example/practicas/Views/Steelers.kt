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
fun Steelers(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Steelers") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFB612)
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
                R.drawable.stelerse,
                "Fundación: 1933 | Ciudad: Pittsburgh, PA\n" +
                        "Super Bowls Ganados: 6 (1974, 1975, 1978, 1979, 2005, 2008)\n" +
                        "Jugadores icónicos: Terry Bradshaw, Troy Polamalu\n",
                Color(0xFFFFB612),Color.Black
            )}
            item{LottieCard(R.raw.redfootballrotating)}
            item{ Curiosidades("Curiosidades:\nEl logo con tres estrellas viene de la industria del acero de Pittsburgh,\n" +
                    "Tienen seis Super Bowls, la mayor cantidad junto con los Patriots,\n" +
                    "Su 'Terrible Towel' es uno de los símbolos más icónicos de la NFL.",Color(0xFFFFB612)) }
        }
    }
}