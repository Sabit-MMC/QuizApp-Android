package com.example.quizapp

class QuizRepository(val apiService: ApiService) {
    suspend fun getQuestions() = apiService.getQuestion()
    suspend fun getLeveledQuestion(level: Int) = apiService.getLeveledQuestion(level)
}