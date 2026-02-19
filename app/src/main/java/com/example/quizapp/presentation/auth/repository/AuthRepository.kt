package com.example.quizapp.presentation.auth.repository

import com.example.quizapp.data.network.KtorClient
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.network.safeApiCall
import com.example.quizapp.presentation.auth.model.AuthRequestBody
import com.example.quizapp.presentation.auth.model.SignInResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepository {
    suspend fun signIn(body: AuthRequestBody): NetworkResult<SignInResponse> {
        return safeApiCall {
            KtorClient.client.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        }
    }

    suspend fun signUp(body: AuthRequestBody): NetworkResult<Boolean> {
        return safeApiCall {
            KtorClient.client.post("/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        }
    }
}
