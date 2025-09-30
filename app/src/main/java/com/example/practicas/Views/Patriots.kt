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
fun Patriots(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Patriots") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF002244)
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("AFC")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TeamInfo(
                team = R.drawable.patriose,
                texto = "Fundación: 1960 | Ciudad: Foxborough, MA\n" +
                        "Super Bowls Ganados: 6 (2001, 2003, 2004, 2014, 2016, 2018)\n" +
                        "Jugadores icónicos: Tom Brady, Rob Gronkowski\n" +
                        "Curiosidad: Dominio absoluto en la AFC Este durante dos décadas",
                cardBackgroundColor = Color(0xFF002244),Color.White)

        }
    }
}

