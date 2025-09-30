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
fun GreenBay(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Green Bay") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF203731)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("NFC")
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
                R.drawable.grenbaye,
                "Fundación: 1919 | Ciudad: Green Bay, WI\n" +
                        "Super Bowls Ganados: 4 (1966, 1967, 1996, 2010)\n" +
                        "Campeonatos de liga pre-Super Bowl: 9\n" +
                        "Jugadores icónicos: Brett Favre, Aaron Rodgers\n" +
                        "Curiosidad: Únicos propiedad de los aficionados (accionistas)",
                Color(0xFF203731),Color.White
            )
        }
    }
}