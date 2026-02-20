package com.example.quizapp.presentation.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizSubmission(
    val userId: String,
    val categoryId: String,
    val level: String,
    val score: Int,
    val totalQuestions: Int
)

@Serializable
data class QuizHistoryItem(
    val id: String,
    val userId: String,
    val categoryId: String,
    val level: String,
    val score: Int,
    val totalQuestions: Int,
    val createdAt: String
)
