package com.example.quizapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.presentation.quiz.QuestionViewModel

@Composable
fun ResultScreen(questionViewModel: QuestionViewModel) {

    val resultState by questionViewModel.getAllResultState.collectAsState()

    LaunchedEffect(Unit) {
        questionViewModel.fetchAllResults()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (resultState?.data?.isEmpty() == true) {
            Text("No data", modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                itemsIndexed(resultState?.data ?: emptyList()) {index,item ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("UserId:${item.userId}")
                        Text("Faiz:${item.percentage}")
                    }
                }
            }
        }
    }
}