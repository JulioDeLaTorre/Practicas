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
fun SF49ers(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("SF49ers") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFB3995D)
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
                R.drawable.sf49erse,
                "Fundación: 1946 | Ciudad: Santa Clara, CA\n" +
                        "Super Bowls Ganados: 5 (1981, 1984, 1988, 1989, 1994)\n" +
                        "Jugadores icónicos: Joe Montana, Steve Young\n" +
                        "Curiosidad: Famosa era de los 80 y 90 con dominación total",
                Color(0xFFB3995D)
            )
        }
    }
}