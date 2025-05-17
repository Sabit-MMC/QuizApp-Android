package com.example.quizapp.data.model.auth

data class RefreshRequest(
    val userId: String,
    val refreshToken: String
)
