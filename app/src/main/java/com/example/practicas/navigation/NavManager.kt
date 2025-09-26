package com.example.practica4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practica4.Views.SSCoyboys
import com.example.practica4.Views.SSDolphins
import com.example.practica4.Views.SSEagles
import com.example.practica4.Views.SSF49ers
import com.example.practica4.Views.SSGreenBay
import com.example.practica4.Views.SSKansas
import com.example.practica4.Views.SSPatriots
import com.example.practica4.Views.SSteelers
import com.example.segundodia.Views.HomeView
import com.example.practica4.Views.SplashScreen
import com.example.practicas.Views.Cowboys
import com.example.practicas.Views.Dolphins
import com.example.practicas.Views.Eagles
import com.example.practicas.Views.GreenBay
import com.example.practicas.Views.Kansas
import com.example.practicas.Views.Patriots
import com.example.practicas.Views.SF49ers
import com.example.practicas.Views.Steelers
import com.example.segundodia.Views.AFC
import com.example.segundodia.Views.NFC

@Composable
fun NavManager(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "Splash"){

        composable("Home"){
            HomeView(navController)
        }

        composable("Splash"){
            SplashScreen(navController)
        }

        composable("AFC"){
            AFC(navController)
        }

        composable("NFC") {
            NFC(navController)
        }

        composable("patriots") {
            Patriots(navController)
        }
        composable("SSPatriots") {
            SSPatriots(navController)
        }

        composable("steelers") {
            Steelers(navController)
        }
        composable("SSteelers") {
            SSteelers(navController)
        }

        composable("kansas") {
            Kansas(navController)
        }
        composable("SSKansas") {
            SSKansas(navController)
        }

        composable("dolphins") {
            Dolphins(navController)
        }
        composable("SSDolphins") {
            SSDolphins(navController)
        }

        composable("cowboys") {
            Cowboys(navController)
        }
        composable("SSCowboys") {
            SSCoyboys(navController)
        }

        composable("49ers") {
            SF49ers(navController)
        }
        composable("SSF49ers") {
            SSF49ers(navController)
        }

        composable("green bay") {
            GreenBay(navController)
        }
        composable("SSGreenBay") {
            SSGreenBay(navController)
        }

        composable("eagles") {
            Eagles(navController)
        }
        composable("SSEagles") {
            SSEagles(navController)
        }
    }
}
