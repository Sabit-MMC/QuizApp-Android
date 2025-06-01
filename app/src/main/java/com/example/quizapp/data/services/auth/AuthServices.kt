package com.example.quizapp.data.services.auth

import com.example.quizapp.data.model.auth.LoginResponse
import com.example.quizapp.data.model.auth.RefreshRequest
import com.example.quizapp.data.model.auth.RefreshTokenResponse
import com.example.quizapp.data.model.auth.RegisterResponse
import com.example.quizapp.data.model.auth.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {
    @POST("/auth/register")
    suspend fun register(@Body request: UserRequest): Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: UserRequest): Response<Boolean>

    @POST("/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<RefreshTokenResponse>
}