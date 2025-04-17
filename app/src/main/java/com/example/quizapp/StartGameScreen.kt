package com.example.quizapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.navigation.QuizNavHostObject

@Composable
fun StartGameScreen(navHostController: NavHostController, viewModel: QuestionViewModel) {
    val questionStateValue = viewModel.questionState.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        questionStateValue?.data?.levels?.forEachIndexed { index, level ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, Color.Blue)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .clickable {
                        navHostController.navigate("${QuizNavHostObject.QuizScreen}/${level.level}")
                    }
            ) {
                Text(
                    "Sual Nomre ${level.level}", modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }
        }
    }
}