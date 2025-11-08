package com.example.quizapp.presentation.quiz.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.quizapp.presentation.quiz.QuestionViewModel

@Composable
fun StartGameScreen(
    navHostController: NavHostController,
    viewModel: QuestionViewModel
) {

    Box(modifier = Modifier.fillMaxSize()){
        Text("Test", modifier = Modifier.align(Alignment.Center))
    }

}