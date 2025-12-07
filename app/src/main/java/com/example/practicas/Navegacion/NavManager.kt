package com.example.practicas.Navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicas.Vistas.DetailView
import com.example.practicas.Vistas.HomeView

@Composable
fun NavManager() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        // 1. Pantalla Principal (Lista de Agentes)
        composable("Home") {
            HomeView(navController)
        }

        // 2. Pantalla de Detalle (Dinámica)
        // La ruta espera recibir algo como "Detail/e370fa57-..."
        composable(
            route = "Detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Recuperamos el ID que nos mandaron desde el Home
            val id = backStackEntry.arguments?.getString("id") ?: ""

            // Se lo pasamos a la vista de detalle
            DetailView(navController, id,
                onBack = { navController.popBackStack() })
        }
    }
}