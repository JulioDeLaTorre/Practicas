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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.segundodia.components.ActionButton
import com.example.segundodia.components.MainButton
import com.example.segundodia.components.MainIconButton
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NFC(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("National Football Conference") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1D428A)
                ),navigationIcon= {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.navigate("Home")
                    }
                })
        }, floatingActionButton = {
            ActionButton()
        }
    ){
        ContentNFC(navController)
    }
}
@Composable
fun ContentNFC(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
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
                    painter = painterResource(id = R.drawable.dallasc),
                    contentDescription = "Cowboys",
                    modifier = Modifier.size(80.dp)
                )
                MainButton("Cowboys", Color(0xFF869397), Color.White) {
                    navController.navigate("SSCowboys")
                }
            }
            Spacer(Modifier.width(80.dp))
            Column(){
                Image(
                    painter = painterResource(id = R.drawable.sf49ers),
                    contentDescription = "49ers",
                    modifier = Modifier.size(80.dp)
                )

                MainButton("49ers", Color(0xFFAA0000), Color.White) {
                    navController.navigate("SSF49ers") }
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
                    painter = painterResource(id = R.drawable.greenbay),
                    contentDescription = "Green Bay",
                    modifier = Modifier.size(80.dp)
                )

                MainButton("Green Bay", Color(0xFF203731), Color.White) {
                    navController.navigate("SSGreenBay")
                }
            }
            Spacer(Modifier.width(80.dp))
            Column() {
                Image(
                    painter = painterResource(id = R.drawable.eagles),
                    contentDescription = "Eagles",
                    modifier = Modifier.size(80.dp)
                )
                MainButton("Eagles", Color(0xFF004C54), Color.White) {
                    navController.navigate("SSEagles")
                }
            }
        }
    }
}