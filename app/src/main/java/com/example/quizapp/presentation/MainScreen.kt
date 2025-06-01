package com.example.quizapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.presentation.profil.ProfileScreen
import com.example.quizapp.presentation.quiz.QuestionViewModel
import com.example.quizapp.presentation.quiz.QuizNavHost
import com.example.quizapp.tools.DataStoreHelper

@Composable
fun MainScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel,
    addQuestionViewModel: AddQuestionViewModel,
    dataStoreHelper: DataStoreHelper
) {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Quiz, BottomNavItem.Result,BottomNavItem.Profile,
        BottomNavItem.AddQuestion)
    var showBottomBar by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if(showBottomBar) {
                CustomNavigationBar(
                    items = items,
                    navController = navController,
                    height = 56.dp
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Quiz.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Quiz.route) {
                QuizNavHost(
                    navHostController = navHostController,
                    questionViewModel = questionViewModel,
                    dataStoreHelper = dataStoreHelper,
                    showBottomBar = {visible -> showBottomBar = visible}
                )
            }
            composable (BottomNavItem.Result.route){
                ResultScreen(questionViewModel)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(viewModel = authViewModel, dataStoreHelper = dataStoreHelper)
            }
            composable(BottomNavItem.AddQuestion.route) {
                AddQuestionScreen { question, options, correctIndex, level, imageFile ->
                    addQuestionViewModel.uploadQuiz(
                        question = question,
                        options = options,
                        correctAnswerIndex = correctIndex,
                        level = level,
                        imageFile = imageFile
                    )
                }
            }
        }
    }
}

@Composable
fun CustomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route

    Surface(
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.height(height)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentDestination == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { },
                    label = { Text(item.title) }
                )
            }
        }
    }
}