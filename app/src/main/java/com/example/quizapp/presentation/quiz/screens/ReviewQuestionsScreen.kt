package com.example.quizapp.presentation.quiz.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quizapp.presentation.quiz.HomeViewModel
import com.example.quizapp.presentation.quiz.model.Option
import com.example.quizapp.presentation.quiz.model.Question
import com.example.quizapp.ui.theme.CorrectAnswer
import com.example.quizapp.ui.theme.CorrectAnswerContainer
import com.example.quizapp.ui.theme.IncorrectAnswer
import com.example.quizapp.ui.theme.IncorrectAnswerContainer

@Composable
fun ReviewQuestionsScreen(
    viewModel: HomeViewModel,
    onBackClick: () -> Unit
) {
    val questions = viewModel.questions
    val userAnswers = viewModel.userAnswers

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Review Answers",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            itemsIndexed(questions) { index, question ->
                ReviewQuestionItem(
                    question = question,
                    questionNumber = index + 1,
                    totalQuestions = questions.size,
                    selectedOptionId = userAnswers[question.id]
                )
            }
        }
    }
}

@Composable
fun ReviewQuestionItem(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    selectedOptionId: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Question $questionNumber/$totalQuestions",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.text.az, // Assuming 'az' locale
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEach { option ->
            ReviewOptionItem(
                option = option,
                isSelected = selectedOptionId == option.id,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Explanation
        val explanationText = question.explanation.az // Assuming 'az' locale
        if (explanationText.isNotBlank()) {
            Text(
                text = "Explanation",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = explanationText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ReviewOptionItem(
    option: Option,
    isSelected: Boolean,
) {
    val isCorrect = option.isCorrect

    val borderColor = when {
        isCorrect -> CorrectAnswer
        isSelected && !isCorrect -> IncorrectAnswer
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val backgroundColor = when {
        isCorrect -> CorrectAnswerContainer
        isSelected && !isCorrect -> IncorrectAnswerContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isCorrect -> CorrectAnswer
        isSelected && !isCorrect -> IncorrectAnswer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option.text.az, // Assuming 'az' locale
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (isCorrect) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Correct Answer",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(CorrectAnswer)
                            .padding(4.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Incorrect Answer",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(IncorrectAnswer)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}
