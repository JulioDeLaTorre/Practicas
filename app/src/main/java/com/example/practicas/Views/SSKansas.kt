package com.example.practica4.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.practicas.R
import kotlinx.coroutines.delay

@Composable
fun SSKansas(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(500)
            navController.navigate("kansas") {
            popUpTo("AFC") {
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.kansasl),
            contentDescription = "Logo"
        )
    }
}
