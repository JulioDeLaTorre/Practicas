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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TeamInfo(
                R.drawable.stelerse,
                "Fundación: 1933 | Ciudad: Pittsburgh, PA\n" +
                        "Super Bowls Ganados: 6 (1974, 1975, 1978, 1979, 2005, 2008)\n" +
                        "Jugadores icónicos: Terry Bradshaw, Troy Polamalu\n" +
                        "Curiosidad: Famosa defensa “Steel Curtain” en los años 70",
                Color(0xFFFFB612),Color.Black
            )
        }
    }
}