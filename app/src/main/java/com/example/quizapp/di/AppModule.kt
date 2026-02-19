package com.example.quizapp.di

import com.example.quizapp.data.tools.DataStoreConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { DataStoreConfig(androidContext()) }
}
