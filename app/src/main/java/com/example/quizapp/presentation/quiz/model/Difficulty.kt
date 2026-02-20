package com.example.quizapp.presentation.quiz.model

import androidx.compose.ui.graphics.Color

sealed class Difficulty(
    val title: String,
    val subtitle: String,
    val color: Color,
    val keyword: String,
    val questionsCount: Int? = null
) {
    data object Simple : Difficulty(title = "Simple", subtitle = "Beginner friendly", Color(0xFF4CAF50), keyword = "SIMPLE")
    data object Medium : Difficulty(title = "Medium", subtitle = "Standard challenge", Color(0xFFFF9800), keyword = "MEDIUM")
    data object Advance : Difficulty(title = "Advance", subtitle = "For experts only", Color(0xFFF44336), keyword = "ADVANCE")

    companion object {
        val all: List<Difficulty> get() = listOf(Simple, Medium, Advance)
    }
}
