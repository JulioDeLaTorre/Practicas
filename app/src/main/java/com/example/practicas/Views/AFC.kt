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
fun AFC(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("American Football Conference") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFC8102E)
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("Home")
                    }
                }
            )
        },
    ) { paddingValues ->
        ContentAFC(navController, Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentAFC(navController: NavController, modifier: Modifier) {
    val afcTeams = listOf(
        Team(
            "Patriots",
            "New England Patriots",
            R.drawable.patriotsc,
            Color(0xFF002244),
            Color.White,
            "SSPatriots"
        ),
        Team(
            "Steelers",
            "Pittsburgh Steelers",
            R.drawable.steleersc,
            Color.Black,
            Color.White,
            "SSteelers"
        ),
        Team(
            "Chiefs",
            "Kansas City Chiefs",
            R.drawable.kansasc,
            Color(0xFFE31837),
            Color.White,
            "SSKansas"
        ),
        Team(
            "Dolphins",
            "Miami Dolphins",
            R.drawable.dolphinsc,
            Color(0xFF008E97),
            Color.White,
            "SSDolphins"
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
        items(afcTeams) { team ->
            TeamRow(team = team, navController = navController)
        }
    }
}
