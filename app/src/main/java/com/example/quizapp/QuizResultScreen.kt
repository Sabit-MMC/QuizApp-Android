package com.example.quizapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.navigation.QuizNavHostObject

@Composable
fun QuizResultScreen(
    navHostController: NavHostController,
    viewModel: QuestionViewModel
) {
    val result = viewModel.quizResult

    LaunchedEffect(Unit) {
        viewModel.fetchAllResults()
    }

    BackConfirmHandler(
        onConfirmExit = {
            viewModel.clearViewModel()
            navHostController.navigate(QuizNavHostObject.StartGameScreen)
        },
        dialogText = "Səhifədən çıxmaq istəyirsiniz? Verilən cavablar silinəcək."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Səviyyə: ${result.level}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Ümumi sual: ${result.totalQuestions}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Düzgün cavab: ${result.correctCount}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Faiz: ${result.percentage.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (result.percentage >= 50) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton(
            onClick = {
                viewModel.clearViewModel()
                navHostController.navigate(QuizNavHostObject.StartGameScreen) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Quizlərə geri qayıt")
        }
    }
}