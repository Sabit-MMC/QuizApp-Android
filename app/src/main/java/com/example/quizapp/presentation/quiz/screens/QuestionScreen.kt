package com.example.quizapp.presentation.quiz.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.presentation.quiz.HomeViewModel
import com.example.quizapp.ui.theme.CorrectAnswer
import com.example.quizapp.ui.theme.CorrectAnswerContainer
import com.example.quizapp.ui.theme.IncorrectAnswer
import com.example.quizapp.ui.theme.IncorrectAnswerContainer
import com.example.quizapp.ui.theme.NextQuestionButton
import kotlinx.coroutines.delay

@Composable
fun QuestionScreen(
    viewModel: HomeViewModel,
    categoryName: String,
    onBackClick: () -> Unit,
    onQuizFinish: () -> Unit
) {
    val questions = viewModel.questions
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(3) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

    fun onNextClick() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            selectedOptionId = null
            isAnswered = false
        } else {
            viewModel.submitQuiz(currentQuestion.categoryId, currentQuestion.level)
            onQuizFinish()
        }
    }

    // Timer Logic
    LaunchedEffect(currentQuestionIndex, isAnswered) {
        if (!isAnswered) {
            timeLeft = 20
            while (timeLeft > 0 && !isAnswered) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft == 0 && !isAnswered) {
                isAnswered = true
                viewModel.recordAnswer(currentQuestion.id, null, null) // Mark as skipped
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check, 
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = categoryName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (currentQuestionIndex < questions.size - 1) {
                    TextButton(onClick = {
                        if (!isAnswered) {
                            viewModel.recordAnswer(currentQuestion.id, null, null)
                            onNextClick()
                        }
                    }) {
                        Text("Skip", color = MaterialTheme.colorScheme.outline)
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Progress Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PROGRESS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() 
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        val progress = (currentQuestionIndex + 1).toFloat() / questions.size
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Timer
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { timeLeft.toFloat() / 20f },
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(
                            text = timeLeft.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "sec",
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Question Text
            Text(
                text = currentQuestion.text.az,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Options
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                currentQuestion.options.forEachIndexed { index, option ->
                    val letter = ('A' + index).toString()
                    OptionItem(
                        letter = letter,
                        text = option.text.az,
                        isSelected = selectedOptionId == option.id,
                        isCorrect = option.isCorrect,
                        isAnswered = isAnswered,
                        onClick = {
                            if (!isAnswered) {
                                selectedOptionId = option.id
                                isAnswered = true
                                viewModel.recordAnswer(currentQuestion.id, option.id, option.isCorrect)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "QUESTION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${currentQuestionIndex + 1}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " / ${questions.size}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = { onNextClick() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NextQuestionButton,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = isAnswered
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (currentQuestionIndex < questions.size - 1) "Next Question" else "Finish Quiz",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    letter: String,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isAnswered && isCorrect -> CorrectAnswer
        isAnswered && isSelected && !isCorrect -> IncorrectAnswer
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val backgroundColor = when {
        isAnswered && isCorrect -> CorrectAnswerContainer
        isAnswered && isSelected && !isCorrect -> IncorrectAnswerContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isAnswered && isCorrect -> CorrectAnswer
        isAnswered && isSelected && !isCorrect -> IncorrectAnswer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(
            if (isAnswered && (isCorrect || isSelected)) 2.dp else 1.dp,
            borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            if (isAnswered) {
                if (isCorrect) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(CorrectAnswer)
                            .padding(4.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(IncorrectAnswer)
                            .padding(4.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )
            }
        }
    }
}
