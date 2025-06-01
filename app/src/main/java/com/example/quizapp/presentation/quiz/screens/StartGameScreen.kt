package com.example.quizapp.presentation.quiz.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.presentation.quiz.QuestionViewModel
import com.example.quizapp.presentation.quiz.QuizNavHostObject
import com.example.quizapp.tools.Status
import com.example.quizapp.tools.components.BackConfirmHandler

@Composable
fun StartGameScreen(
    navHostController: NavHostController,
    viewModel: QuestionViewModel
) {
    val levelStatusStateValue = viewModel.levelStatusState.collectAsState().value
    LaunchedEffect (Unit) {
        viewModel.getLevels()
    }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        when (levelStatusStateValue?.status) {
            Status.LOADING -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Status.CONTENT -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(levelStatusStateValue.data?.size ?: 0) { index ->
                        val level = levelStatusStateValue.data?.get(index)?.level ?: 1
                        val isLocked =
                            levelStatusStateValue.data?.get(index)?.isUnlocked == false
                        LevelsItem(navHostController, viewModel, level, isLocked)
                    }
                }
            }

            Status.ERROR -> {
                Toast.makeText(context, "Error occur", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Text("No data", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    BackConfirmHandler(
        onConfirmExit = {
            (context as Activity).finish()
        },
        dialogText = "Tətbiqdən çıxmaq istəyirsiniz?"
    )

}

@Composable
fun LevelsItem(
    navHostController: NavHostController,
    viewModel: QuestionViewModel,
    level: Int,
    isLocked: Boolean
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!isLocked) {
                    viewModel.clearViewModel()
                    navHostController.navigate("${QuizNavHostObject.QuizScreen}/$level")
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isLocked) {
                    Icon(Icons.Default.Lock, "lock")
                }

                Text(
                    text = "Quiz $level",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}