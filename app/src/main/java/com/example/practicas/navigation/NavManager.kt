package com.example.practicas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicas.viewModels.LoginViewModel
import com.example.practicas.viewModels.NotesViewModel
import com.example.practicas.views.login.BlankView
import com.example.practicas.views.login.ChangePasswordView
import com.example.practicas.views.login.ForgotPasswordView
import com.example.practicas.views.login.ProfileView
import com.example.practicas.views.login.TabsView
import com.example.practicas.views.login.VerifyCodeView
import com.example.practicas.views.notes.AddNoteView
import com.example.practicas.views.notes.EditNoteView
import com.example.practicas.views.notes.HomeView

@Composable
fun NavManager(loginVM: LoginViewModel, notesVM: NotesViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Blank" ){
        composable("Blank"){
            BlankView(navController)
        }
        composable("Login"){
            TabsView(navController, loginVM)
        }

        composable("ForgotPassword"){
            ForgotPasswordView(navController, loginVM)
        }

        composable("VerifyCodeView"){
            VerifyCodeView(navController, loginVM)
        }

        composable("ProfileView"){
            ProfileView(navController, loginVM)
        }
        composable("ChangePasswordView"){
            ChangePasswordView(navController, loginVM)
        }

        composable("Home"){
            HomeView(navController, notesVM)
        }
        composable("AddNoteView"){
            AddNoteView(navController, notesVM)
        }
        composable("EditNoteView/{idDoc}", arguments = listOf(
            navArgument("idDoc") { type = NavType.StringType }
        )){
            val idDoc = it.arguments?.getString("idDoc") ?: ""
            EditNoteView(navController, notesVM, idDoc)
        }
    }
}