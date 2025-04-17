package com.example.quizapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.navigation.QuizNavHostObject

@Composable
fun QuestionScreen(navHostController: NavHostController,viewModel: QuestionViewModel,level: Int) {

    val leveledQuestionStateValue = viewModel.leveledQuestionState.collectAsState().value
    var selectedIndex by remember { mutableIntStateOf(0) }
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getLeveledQuestion(level)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when (leveledQuestionStateValue?.status) {

            Status.LOADING -> {
                CircularProgressIndicator()
            }

            Status.CONTENT -> {

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sual")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(leveledQuestionStateValue.data?.questions?.get(currentIndex)?.question ?: "")
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier = Modifier) {
                        leveledQuestionStateValue.data?.questions?.get(currentIndex)?.options?.forEachIndexed { index, option ->
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = viewModel.selectedOption == index,
                                    onClick = {
                                        selectedIndex = index
                                        viewModel.onOptionSelected(index)
                                    })
                                Text(option)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
        Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = {
            if (selectedIndex == leveledQuestionStateValue?.data?.questions?.get(currentIndex)?.correctAnswerIndex && currentIndex < leveledQuestionStateValue.data?.questions?.size!!.minus(1)) {
                currentIndex += 1
                viewModel.clearSelectedOption()
            }else {
                navHostController.navigate(QuizNavHostObject.QuizResultScreen)
            }
        }) {
            if (currentIndex == leveledQuestionStateValue?.data?.questions?.size?.minus(1)) {
                Text("Quizi bitir")
            } else
                Text("Novbeti sual")
        }
    }
}