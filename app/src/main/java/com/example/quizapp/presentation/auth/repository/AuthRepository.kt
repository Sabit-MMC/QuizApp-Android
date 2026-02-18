package com.example.quizapp.presentation.auth.repository

import com.example.quizapp.data.network.KtorClient
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.network.safeApiCall
import com.example.quizapp.presentation.auth.model.SignInResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

class AuthRepository {
    suspend fun signIn(): NetworkResult<SignInResponse> {
        return safeApiCall {
            KtorClient.client.get("/auth/login").body()
        }
    }

    suspend fun signUp(): NetworkResult<Boolean> {
        return safeApiCall {
            KtorClient.client.get("/auth/register").body()
        }
    }
}