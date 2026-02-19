package com.example.quizapp.presentation.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestBody(
    val userId: String,
    val password: String
)
