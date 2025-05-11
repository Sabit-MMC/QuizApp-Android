package com.example.quizapp

import com.example.quizapp.model.AllLevelsQuestions
import com.example.quizapp.model.LevelQuestionGroup
import com.example.quizapp.model.QuizResult
import com.example.quizapp.model.QuizSubmissionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/quiz")
    suspend fun getQuestion():Response<AllLevelsQuestions>

    @GET("/quizByLevel")
    suspend fun getLeveledQuestion(
        @Query("level") level: Int
    ):Response<LevelQuestionGroup>

    @POST("/quiz/submit")
    suspend fun submitQuiz(@Body request: QuizSubmissionRequest): Response<QuizResult>

    @POST("/quiz/result")
    suspend fun insertQuiz(@Body request: QuizResult): Response<QuizResult>

    @GET("/quiz/result")
    suspend fun getAllResult(): Response<List<QuizResult>>
}