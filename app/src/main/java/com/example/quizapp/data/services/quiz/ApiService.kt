package com.example.quizapp.data.services.quiz

import com.example.quizapp.data.model.AllLevelsQuestions
import com.example.quizapp.data.model.LevelQuestionGroup
import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.model.QuizSubmissionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/quiz")
    suspend fun getQuestion(): Response<AllLevelsQuestions>

    @GET("/quizByLevel")
    suspend fun getLeveledQuestion(
        @Query("level") level: Int
    ): Response<LevelQuestionGroup>

    @POST("/quiz/submit")
    suspend fun submitQuiz(@Body request: QuizSubmissionRequest): Response<QuizResult>

    @POST("/quiz/result")
    suspend fun insertQuiz(@Body request: QuizResult): Response<QuizResult>

    @GET("/quiz/result")
    suspend fun getAllResult(): Response<List<QuizResult>>
}