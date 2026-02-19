package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.quizapp.data.tools.DataStoreConfig
import com.example.quizapp.presentation.auth.AuthActivity
import com.example.quizapp.presentation.quiz.HomeActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject

class LauncherActivity : ComponentActivity() {
    private val dataStoreConfig: DataStoreConfig by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                LaunchedEffect(Unit) {
                    val isLoggedIn = dataStoreConfig.isLoggedIn.first()
                    val intent = if (isLoggedIn) {
                        Intent(this@LauncherActivity, HomeActivity::class.java)
                    } else {
                        Intent(this@LauncherActivity, AuthActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
