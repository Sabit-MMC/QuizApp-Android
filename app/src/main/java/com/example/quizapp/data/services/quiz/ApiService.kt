package com.example.quizapp.data.services.quiz

import com.example.quizapp.data.model.AllLevelsQuestions
import com.example.quizapp.data.model.Category
import com.example.quizapp.data.model.LevelQuestionGroup
import com.example.quizapp.data.model.LevelStatus
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.model.QuizSubmissionRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ApiService {
    @GET("/level")
    suspend fun getLevels(@Query("userId") userId: String): Response<List<LevelStatus>>

    @GET("/quiz-by-level")
    suspend fun getLeveledQuestion(
        @Query("level") level: Int
    ): Response<LevelQuestionGroup>

    @POST("/result")
    suspend fun insertQuiz(@Body request: QuizSubmissionRequest): Response<QuizResult>

    @Multipart
    @POST("/upload-question")
    suspend fun uploadQuestion(@PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>,
                               @Part image: MultipartBody.Part?): Response<Question>
}