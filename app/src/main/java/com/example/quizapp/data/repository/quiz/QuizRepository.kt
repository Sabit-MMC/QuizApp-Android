package com.example.quizapp.data.repository.quiz

import com.example.quizapp.data.model.QuizSubmissionRequest
import com.example.quizapp.data.services.quiz.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.PartMap

class QuizRepository(val apiService: ApiService) {
    suspend fun getLevels(userId: String) = apiService.getLevels(userId)
    suspend fun getLeveledQuestion(level: Int) = apiService.getLeveledQuestion(level)
    suspend fun insertResult(quizResult: QuizSubmissionRequest) = apiService.insertQuiz(quizResult)
    suspend fun uploadQuestion(@PartMap requestBodyMap: Map<String, @JvmSuppressWildcards RequestBody>,partBody: MultipartBody.Part?) = apiService.uploadQuestion(requestBodyMap,partBody)
}