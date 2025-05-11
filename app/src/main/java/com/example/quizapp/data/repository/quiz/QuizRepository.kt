package com.example.quizapp.data.repository.quiz

import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.model.QuizSubmissionRequest
import com.example.quizapp.data.services.quiz.ApiService

class QuizRepository(val apiService: ApiService) {
    suspend fun getQuestions() = apiService.getQuestion()
    suspend fun getLeveledQuestion(level: Int) = apiService.getLeveledQuestion(level)
    suspend fun submitQuiz(quizSubmissionRequest: QuizSubmissionRequest) = apiService.submitQuiz(quizSubmissionRequest)
    suspend fun insertQuiz(quizResult: QuizResult) = apiService.insertQuiz(quizResult)
    suspend fun fetchAllResults() = apiService.getAllResult()
}