package com.example.quizapp.presentation.quiz.model

import androidx.compose.ui.graphics.Color

sealed class Difficulty(
    val title: String,
    val subtitle: String,
    val questionsCount: Int,
    val color: Color
) {
    data object Simple : Difficulty("Simple", "Beginner friendly", 5, Color(0xFF4CAF50))
    data object Medium : Difficulty("Medium", "Standard challenge", 10, Color(0xFFFF9800))
    data object Advanced : Difficulty("Advanced", "For experts only", 20, Color(0xFFF44336))

    companion object {
        val all: List<Difficulty> get() = listOf(Simple, Medium, Advanced)
    }
}
