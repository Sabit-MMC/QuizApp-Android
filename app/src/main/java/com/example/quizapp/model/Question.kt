package com.example.quizapp.model

data class AllLevelsQuestions(
    val levels: List<LevelQuestionGroup>
)

data class LevelQuestionGroup(
    val level: Int,
    val questions: List<Question>
)

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)