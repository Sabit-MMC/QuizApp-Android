package com.example.quizapp

import com.example.quizapp.model.AllLevelsQuestions
import com.example.quizapp.model.LevelQuestionGroup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/quiz")
    suspend fun getQuestion():Response<AllLevelsQuestions>

    @GET("/quizByLevel")
    suspend fun getLeveledQuestion(
        @Query("level") level: Int
    ):Response<LevelQuestionGroup>
}