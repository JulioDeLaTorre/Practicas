package com.example.segundodia.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.R
import com.example.segundodia.components.MainButton
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AFC(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("American Football Conference") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFC8102E)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("Home")
                    }
                })
        },
    ){
        ContentAFC(navController)
    }
}
@Composable
fun ContentAFC(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(){
                Image(
                    painter = painterResource(id = R.drawable.patriots),
                    contentDescription = "Patriots",
                    modifier = Modifier.size(80.dp)
                )
                MainButton("Patriots", Color(0xFF002244), Color.White) {
                    navController.navigate("SSPatriots")
                }
            }
            Spacer(Modifier.width(80.dp))
            Column(){
                Image(
                    painter = painterResource(id = R.drawable.steelers),
                    contentDescription = "Steelers",
                    modifier = Modifier.size(80.dp)
                )

                MainButton("Steelers", Color(0xFFFFB612), Color.Black) {
                    navController.navigate("SSteelers") }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(){
                Image(
                    painter = painterResource(id = R.drawable.kansasc),
                    contentDescription = "kansas",
                    modifier = Modifier.size(80.dp)
                )

                MainButton("Kansas", Color(0xFFE31837), Color.White) {
                    navController.navigate("SSKansas")
                }
            }
            Spacer(Modifier.width(80.dp))
            Column() {
                Image(
                    painter = painterResource(id = R.drawable.dolphins),
                    contentDescription = "dolphins",
                    modifier = Modifier.size(80.dp)
                )
                MainButton("Dolphins", Color(0xFF008E97), Color.White) {
                    navController.navigate("SSDolphins")
                }
            }
        }
    }
}