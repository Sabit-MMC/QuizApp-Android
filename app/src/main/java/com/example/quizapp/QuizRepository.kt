package com.example.quizapp

import com.example.quizapp.model.QuizResult
import com.example.quizapp.model.QuizSubmissionRequest

class QuizRepository(val apiService: ApiService) {
    suspend fun getQuestions() = apiService.getQuestion()
    suspend fun getLeveledQuestion(level: Int) = apiService.getLeveledQuestion(level)
    suspend fun submitQuiz(quizSubmissionRequest: QuizSubmissionRequest) = apiService.submitQuiz(quizSubmissionRequest)
    suspend fun insertQuiz(quizResult: QuizResult) = apiService.insertQuiz(quizResult)
    suspend fun fetchAllResults() = apiService.getAllResult()
}