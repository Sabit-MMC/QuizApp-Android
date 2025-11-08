package com.example.quizapp.data.repository.quiz

import com.example.quizapp.data.services.quiz.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.PartMap

class QuizRepository(val apiService: ApiService) {}