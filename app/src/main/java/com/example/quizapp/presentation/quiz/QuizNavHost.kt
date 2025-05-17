package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.presentation.quiz.screens.QuestionScreen
import com.example.quizapp.presentation.quiz.screens.QuizResultScreen
import com.example.quizapp.presentation.quiz.screens.StartGameScreen
import com.example.quizapp.tools.DataStoreHelper

@Composable
fun QuizNavHost(
    navHostController: NavHostController,
    questionViewModel: QuestionViewModel,
    dataStoreHelper: DataStoreHelper,
    showBottomBar: (Boolean) -> Unit
) {

    NavHost(
        navController = navHostController,
        startDestination = QuizNavHostObject.StartGameScreen
    ) {
        composable(route = QuizNavHostObject.StartGameScreen) {
            showBottomBar(true)
            StartGameScreen(navHostController, questionViewModel)
        }
        composable(
            route = "${QuizNavHostObject.QuizScreen}/{level}",
            arguments = listOf(navArgument("level") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            showBottomBar(false)
            val level = backStackEntry.arguments?.getInt("level") ?: 1
            QuestionScreen(navHostController, questionViewModel, level)
        }
        composable(route = QuizNavHostObject.QuizResultScreen) {
            showBottomBar(false)
            QuizResultScreen(navHostController, questionViewModel, dataStoreHelper)
        }
    }
}

object QuizNavHostObject {
    const val StartGameScreen = "startGameScreen"
    const val QuizScreen = "quizScreen"
    const val QuizResultScreen = "quizResultScreen"
}