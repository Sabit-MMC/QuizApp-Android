package com.example.quizapp

import android.app.Application
import com.example.quizapp.di.appModule
import com.example.quizapp.di.authModules
import com.example.quizapp.di.quizModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@QuizApplication)
            modules(appModule, authModules, quizModules)
        }
    }
}
