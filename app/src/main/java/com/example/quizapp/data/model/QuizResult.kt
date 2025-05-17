package com.example.quizapp.data.model

data class QuizResult(
    val userId: String,
    val level: Int,
    val correctCount: Int,
    val totalQuestions: Int,
    val percentage: Double
)
