package com.example.practica4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.segundodia.Views.HomeView
import com.example.practica4.Views.SplashScreen
import com.example.practicas.R
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
            SplashScreen(navController,"Home","Home", R.drawable.nfl_logo,"logo nfl")
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
            SplashScreen(navController,"patriots","AFC",R.drawable.patriotsl,"Logo patriots")
        }

        composable("steelers") {
            Steelers(navController)
        }
        composable("SSteelers") {
            SplashScreen(navController,"steelers","AFC",R.drawable.steelersl,"logo steelers")
        }

        composable("kansas") {
            Kansas(navController)
        }
        composable("SSKansas") {
            SplashScreen(navController,"kansas","AFC",R.drawable.kansasl,"logo chiefs")
        }

        composable("dolphins") {
            Dolphins(navController)
        }
        composable("SSDolphins") {
            SplashScreen(navController,"dolphins","AFC",R.drawable.dolphinsl,"logo dolhpins")
        }

        composable("cowboys") {
            Cowboys(navController)
        }
        composable("SSCowboys") {
            SplashScreen(navController,"cowboys","NFC",R.drawable.dallasl,"logo cowboys")
        }

        composable("49ers") {
            SF49ers(navController)
        }
        composable("SSF49ers") {
            SplashScreen(navController,"49ers","NFC",R.drawable.sf49ersl,"logo 49ers")
        }

        composable("green bay") {
            GreenBay(navController)
        }
        composable("SSGreenBay") {
            SplashScreen(navController,"green bay","NFC",R.drawable.greenbayl,"logo greenbay")
        }

        composable("eagles") {
            Eagles(navController)
        }
        composable("SSEagles") {
            SplashScreen(navController,"eagles","NFC",R.drawable.eaglesl,"logo eagles")
        }
    }
}
