package com.example.segundodia.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practicas.R
import com.example.segundodia.components.MainButton
import com.example.segundodia.components.Space
import com.example.segundodia.components.TextView
import com.example.segundodia.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeView(navController: NavController){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar("Conferencias") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF002244)
                )
            )
        },
    ){
        ContentHomeView(navController)
    }
}
@Composable
fun ContentHomeView(navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize() // Fills the entire screen
        .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)){

        // --- AFC Row ---
        Row(
            // Use weight(1f) to make this Row take up half the vertical space
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.afc),
                contentDescription = "afc",
                modifier = Modifier
                    .size(150.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "American Football Conference",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                MainButton(
                    name = "AFC",
                    Color.Red,
                    Color.White
                ) {
                    navController.navigate("AFC")
                }
            }
        }

        // --- NFC Row ---
        Row(
            // Use weight(1f) to make this Row take up the other half of the vertical space
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.nfc),
                contentDescription = "nfc",
                modifier = Modifier
                    .size(150.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "National Football Conference",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )

                Spacer(modifier = Modifier.height(8.dp))

                MainButton(
                    name = "NFC",
                    Color.Blue,
                    Color.White
                ) {
                    navController.navigate("NFC")
                }
            }
        }
    }
}