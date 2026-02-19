package com.example.quizapp.di

import com.example.quizapp.presentation.quiz.HomeViewModel
import com.example.quizapp.presentation.quiz.repository.QuizRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val quizModules = module {
    single { QuizRepository() }
    viewModel { HomeViewModel(get(),get()) }
}
