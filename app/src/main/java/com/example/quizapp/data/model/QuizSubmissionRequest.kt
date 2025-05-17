package com.example.quizapp.data.model

data class QuizSubmissionRequest(
    val userId: String,
    val level: Int,
    val answers: List<Int>
)
