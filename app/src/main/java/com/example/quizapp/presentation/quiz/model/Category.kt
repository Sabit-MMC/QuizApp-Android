package com.example.quizapp.presentation.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalizedName(
    val az: String,
    val en: String
)

@Serializable
data class Category(
    val id: String,
    val slug: String,
    val name: LocalizedName,
    val iconUrl: String,
    val isActive: Boolean
)
