package com.example.quizapp.di

import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.presentation.auth.repository.AuthRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val authModules = module {
    single { AuthRepository() }
    viewModel { AuthViewModel(get(), get()) }
}
