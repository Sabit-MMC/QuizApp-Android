package com.example.quizapp.data.model

data class QuizResult(
    val level: Int,
    val correctCount: Int,
    val totalQuestions: Int,
    val percentage: Double
)
