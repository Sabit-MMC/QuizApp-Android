package com.example.quizapp.presentation.quiz.repository

import com.example.quizapp.data.network.KtorClient
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.network.safeApiCall
import com.example.quizapp.presentation.quiz.model.Category
import io.ktor.client.call.body
import io.ktor.client.request.get

class QuizRepository {
    suspend fun getCategories(): NetworkResult<List<Category>> {
        return safeApiCall {
            KtorClient.client.get("/categories").body()
        }
    }
}
