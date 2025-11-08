package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapp.presentation.quiz.screens.QuestionScreen
import com.example.quizapp.presentation.quiz.screens.StartGameScreen

@Composable
fun QuizNavHost(
    navHostController: NavHostController,
    questionViewModel: QuestionViewModel,
    showBottomBar: (Boolean) -> Unit
) {

    NavHost(
        navController = navHostController,
        startDestination = QuizNavHostObject.StartGameScreen
    ) {
        composable(
            route = QuizNavHostObject.StartGameScreen
        ) {
            showBottomBar(false)
            StartGameScreen(navHostController, questionViewModel)
        }
    }
}

object QuizNavHostObject {
    const val StartGameScreen = "startGameScreen"
}