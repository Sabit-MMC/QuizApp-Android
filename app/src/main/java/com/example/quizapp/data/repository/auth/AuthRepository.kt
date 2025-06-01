package com.example.quizapp.data.repository.auth

import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.data.services.auth.AuthServices

class AuthRepository(val authServices: AuthServices) {
    suspend fun login(userRequest: UserRequest) = authServices.login(userRequest)
    suspend fun register(userRequest: UserRequest) = authServices.register(userRequest)
}