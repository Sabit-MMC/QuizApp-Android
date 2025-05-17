package com.example.quizapp.presentation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapp.presentation.auth.login.LoginScreen
import com.example.quizapp.presentation.auth.register.RegisterScreen

@Composable
fun AuthNavHost(navHostController: NavHostController, viewModel: AuthViewModel) {

    NavHost(
        navController = navHostController,
        startDestination = AuthNavHostObject.LoginScreen
    ) {
        composable(route = AuthNavHostObject.LoginScreen) {
            LoginScreen(navHostController, viewModel)
        }
        composable(route = AuthNavHostObject.RegisterScreen){
            RegisterScreen(navHostController,viewModel)
        }
    }
}

object AuthNavHostObject {
    const val LoginScreen = "loginScreen"
    const val RegisterScreen = "registerScreen"
}