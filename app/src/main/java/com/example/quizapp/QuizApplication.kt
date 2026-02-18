package com.example.quizapp

import android.app.Application
import com.example.quizapp.di.auth.authModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@QuizApplication)
            modules(authModules)

        }
    }
}