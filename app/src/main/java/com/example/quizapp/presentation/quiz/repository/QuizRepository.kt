package com.example.quizapp.presentation.quiz.repository

import com.example.quizapp.data.network.KtorClient
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.network.safeApiCall
import com.example.quizapp.presentation.quiz.model.Category
import com.example.quizapp.presentation.quiz.model.Question
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class QuizRepository {
    suspend fun getCategories(): NetworkResult<List<Category>> {
        return safeApiCall {
            KtorClient.client.get("/categories").body()
        }
    }

    suspend fun getQuestions(categoryId: String, level: String): NetworkResult<List<Question>> {
        return safeApiCall {
            KtorClient.client.get("/questions") {
                parameter("categoryId", categoryId)
                parameter("level", level)
            }.body()
        }
    }
}
