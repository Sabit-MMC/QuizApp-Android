package com.example.quizapp.model

data class QuizSubmissionRequest(
    val level: Int,
    val answers: List<Int>
)
