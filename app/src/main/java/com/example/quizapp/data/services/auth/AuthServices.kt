package com.example.quizapp.data.services.auth

import com.example.quizapp.data.model.auth.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {
    @POST("/register")
    suspend fun register(@Body request: UserRequest): Response<Map<String, Boolean>>

    @POST("/login")
    suspend fun login(@Body request: UserRequest): Response<Map<String, Boolean>>
}