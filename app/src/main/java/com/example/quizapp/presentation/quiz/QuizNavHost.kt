package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapp.presentation.quiz.screens.QuestionScreen
import com.example.quizapp.presentation.quiz.screens.QuizResultScreen
import com.example.quizapp.presentation.quiz.screens.StartGameScreen

@Composable
fun QuizNavHost(navHostController: NavHostController, viewModel: QuestionViewModel) {

    NavHost(
        navController = navHostController,
        startDestination = QuizNavHostObject.StartGameScreen
    ) {
        composable(route = QuizNavHostObject.StartGameScreen) {
            StartGameScreen(navHostController, viewModel)
        }
        composable(
            route = "${QuizNavHostObject.QuizScreen}/{level}",
            arguments = listOf(navArgument("level") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 1
            QuestionScreen(navHostController,viewModel, level)
        }
        composable(route = QuizNavHostObject.QuizResultScreen){
            QuizResultScreen(navHostController,viewModel)
        }
    }
}

object QuizNavHostObject {
    const val StartGameScreen = "startGameScreen"
    const val QuizScreen = "quizScreen"
    const val QuizResultScreen = "quizResultScreen"
}