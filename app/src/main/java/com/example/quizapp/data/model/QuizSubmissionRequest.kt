package com.example.quizapp.data.model

data class QuizSubmissionRequest(
    val level: Int,
    val answers: List<Int>
)
