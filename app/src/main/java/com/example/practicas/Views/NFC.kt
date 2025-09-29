package com.example.segundodia.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.R
import com.example.practicas.components.Team
import com.example.practicas.components.TeamRow
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NFC(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("National Football Conference") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1D428A) // Color de la NFC
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("Home")
                    }
                }
            )
        },
    ) { paddingValues ->
        // Pasar el padding a ContentNFC
        ContentNFC(navController, Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentNFC(navController: NavController, modifier: Modifier) {
    // 1. Definición de la lista de equipos de la NFC con los nuevos datos
    val nfcTeams = listOf(
        Team(
            "Cowboys",
            "Dallas Cowboys",
            R.drawable.cowboysc, // Asegúrate de que existe
            Color(0xFF869397), // Plata
            Color.White,
            "SSCowboys"
        ),
        Team(
            "49ers",
            "San Francisco 49ers",
            R.drawable.sf49ersc, // Asumo que este es el drawable correcto para 49ers
            Color(0xFFAA0000), // Rojo
            Color.White,
            "SSF49ers"
        ),
        Team(
            "Packers", // Usé Packers como nombre corto para Green Bay
            "Green Bay Packers",
            R.drawable.cascogreenbay, // Asegúrate de que existe
            Color(0xFF203731), // Verde
            Color.White,
            "SSGreenBay"
        ),
        Team(
            "Eagles",
            "Philadelphia Eagles",
            R.drawable.eaglescasco, // Asegúrate de que existe
            Color(0xFF004C54), // Verde
            Color.White,
            "SSEagles"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(nfcTeams) { team ->
            TeamRow(team = team, navController = navController)
        }
    }
}