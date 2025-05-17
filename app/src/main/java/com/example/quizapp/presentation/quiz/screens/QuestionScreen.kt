package com.example.quizapp.presentation.quiz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.presentation.quiz.QuestionViewModel
import com.example.quizapp.presentation.quiz.QuizNavHostObject
import com.example.quizapp.tools.Status
import com.example.quizapp.tools.components.BackConfirmHandler

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navHostController: NavHostController,
    viewModel: QuestionViewModel,
    level: Int
) {
    val leveledQuestionStateValue by viewModel.leveledQuestionState.collectAsState()
    val submitQuizState by viewModel.submitQuizState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isLastQuestion = derivedStateOf {
        viewModel.currentIndex == leveledQuestionStateValue?.data?.questions?.size?.minus(1)
    }
    val context = LocalContext.current

    BackConfirmHandler(
        onConfirmExit = {
            viewModel.clearViewModel()
            navHostController.navigate(QuizNavHostObject.StartGameScreen)
        },
        dialogText = "Səhifədən çıxmaq istəyirsiniz? Verilən cavablar silinəcək."
    )

    LaunchedEffect(level) {
        viewModel.getLeveledQuestion(level)
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onSubmitOrNext(level, isLastQuestion.value)
                    }
                ) {
                    when (submitQuizState?.status) {
                        Status.LOADING -> {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }

                        Status.CONTENT -> {
                            Text(
                                "Quizi Bitir",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            submitQuizState?.data?.let {

                                viewModel.quizResult = it
                                navHostController.navigate(QuizNavHostObject.QuizResultScreen)
                                viewModel.clearSubmitState()
                            }
                        }

                        Status.ERROR -> {
                            Toast.makeText(context, "Xəta baş verdi", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            if (viewModel.currentIndex == leveledQuestionStateValue?.data?.questions?.size?.minus(
                                    1
                                )
                            ) {
                                Text(
                                    "Quizi Bitir",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                Text(
                                    "Növbəti Sual",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (leveledQuestionStateValue?.status) {
                Status.LOADING -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                Status.CONTENT -> {
                    LaunchedEffect(viewModel.currentIndex) {
                        viewModel.startTimer(level = level)
                    }
                    val question =
                        leveledQuestionStateValue?.data?.questions?.get(viewModel.currentIndex)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item{
                            AnimatedTimerBar(timeLeft = timeLeft)
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = question?.question ?: "",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${viewModel.currentIndex + 1}/${leveledQuestionStateValue?.data?.questions?.size}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        items(question?.options?.size ?: 0) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.onOptionSelected(index) },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    RadioButton(
                                        selected = viewModel.selectedOption == index,
                                        onClick = { viewModel.onOptionSelected(index) }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = question?.options?.get(index) ?: "")
                                }
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    Text(
                        "Sualları yükləmək mümkün olmadı.",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}

