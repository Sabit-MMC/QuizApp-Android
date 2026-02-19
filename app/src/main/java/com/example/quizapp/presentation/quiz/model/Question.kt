package com.example.quizapp.presentation.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val categoryId: String,
    val level: String,
    val text: LocalizedText,
    val options: List<Option>,
    val explanation: LocalizedText
)

@Serializable
data class Option(
    val id: String,
    val text: LocalizedText,
    val isCorrect: Boolean
)

@Serializable
data class LocalizedText(
    val az: String,
    val en: String
)
