package com.example.quizapp.presentation.quiz.repository

import com.example.quizapp.data.network.KtorClient
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.network.safeApiCall
import com.example.quizapp.presentation.quiz.model.Category
import com.example.quizapp.presentation.quiz.model.Question
import com.example.quizapp.presentation.quiz.model.QuizHistoryItem
import com.example.quizapp.presentation.quiz.model.QuizSubmission
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

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

    suspend fun submitQuiz(submission: QuizSubmission): NetworkResult<Boolean> {
        return safeApiCall {
            val response: HttpResponse = KtorClient.client.post("/quiz/submit") {
                contentType(ContentType.Application.Json)
                setBody(submission)
            }
            response.status.isSuccess()
        }
    }

    suspend fun getQuizHistory(userId: String, categoryId: String): NetworkResult<List<QuizHistoryItem>> {
        return safeApiCall {
            val response: HttpResponse = KtorClient.client.get("/quiz/history") {
                parameter("userId", userId)
                parameter("categoryId", categoryId)
            }
            if (response.status.isSuccess()) {
                response.body<List<QuizHistoryItem>>()
            } else {
                throw Exception("Server returned ${response.status.value}")
            }
        }
    }
}
