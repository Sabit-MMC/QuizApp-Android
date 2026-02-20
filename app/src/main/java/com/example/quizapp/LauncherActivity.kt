package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.data.tools.DataStoreConfig
import com.example.quizapp.presentation.auth.AuthActivity
import com.example.quizapp.presentation.quiz.HomeActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.android.ext.android.inject

class LauncherActivity : ComponentActivity() {
    private val dataStoreConfig: DataStoreConfig by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val savedDarkMode by dataStoreConfig.isDarkMode.collectAsState(initial = null)
            val isLoggedIn by dataStoreConfig.isLoggedIn.collectAsState(initial = null)

            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn != null) {
                    val destination = if (isLoggedIn == true) HomeActivity::class.java else AuthActivity::class.java
                    val intent = Intent(this@LauncherActivity, destination).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

            QuizAppTheme(darkTheme = savedDarkMode ?: isSystemInDarkTheme()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}
