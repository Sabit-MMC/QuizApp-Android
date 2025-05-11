package com.example.quizapp.presentation.quiz.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.presentation.quiz.QuizNavHostObject
import com.example.quizapp.presentation.quiz.QuestionViewModel

@Composable
fun StartGameScreen(
    navHostController: NavHostController,
    viewModel: QuestionViewModel
) {
    val questionStateValue = viewModel.questionState.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(questionStateValue?.data?.levels?.size ?: 0) { index ->
            val level = questionStateValue?.data?.levels?.get(index)?.level ?: 1
            LevelsItem(navHostController, viewModel, level)
        }
    }
}

@Composable
fun LevelsItem(
    navHostController: NavHostController,
    viewModel: QuestionViewModel,
    level: Int
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.clearViewModel()
                navHostController.navigate("${QuizNavHostObject.QuizScreen}/$level")
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Quiz $level",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}