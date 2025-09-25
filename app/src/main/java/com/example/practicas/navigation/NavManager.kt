package com.example.practica4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.segundodia.Views.DetailsView
import com.example.segundodia.Views.HomeView
import com.example.practica4.Views.SplashScreen

@Composable
fun NavManager(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "Splash"){
        composable("Home"){
            HomeView(navController)
        }
        composable("Detail/{id}",arguments =
            listOf(navArgument("id")
            {type= NavType.IntType })){
            val id=it.arguments?.getInt("id")?:0
            DetailsView(navController,id)
        }
        composable("Splash"){
            SplashScreen(navController)
        }

    }
}
