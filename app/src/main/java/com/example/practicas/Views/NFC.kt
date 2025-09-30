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
import com.example.practicas.components.Team2
import com.example.practicas.components.TeamRow2
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
        ContentNFC(navController, Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentNFC(navController: NavController, modifier: Modifier) {
    val nfcTeams2 = listOf(
        Team2(
            "Cowboys",
            "Dallas Cowboys",
            cardBackgroundColor = Color(0xFFC6C6C6),
            R.drawable.cowboysc,
            buttonColor = Color(0xFF041E42),
            buttonTextColor = Color.White,
            route = "SSCowboys"
        ),
        Team2(
            "49ers",
            "San Francisco 49ers",
            cardBackgroundColor = Color(0xFFB3995D),
            R.drawable.sf49ersc,
            buttonColor = Color(0xFFAA0000),
            buttonTextColor = Color.White,
            route = "SSF49ers"
        ),
        Team2(
            "Packers",
            "Green Bay Packers",
            cardBackgroundColor = Color(0xFFFFB612),
            R.drawable.cascogreenbay,
            buttonColor = Color(0xFF203731),
            buttonTextColor = Color.White,
            route = "SSGreenBay"
        ),
        Team2(
            "Eagles",
            "Philadelphia Eagles",
            cardBackgroundColor = Color(0xFFA5ACAF),
            R.drawable.eaglescasco,
            buttonColor = Color(0xFF004C54),
            buttonTextColor = Color.White,
            route = "SSEagles"
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
        items(nfcTeams2) { team ->
            TeamRow2(team = team, navController = navController)
        }
    }
}