package com.example.quizapp.presentation.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.quizapp.presentation.auth.AuthActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.androidx.compose.koinViewModel

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homeViewModel: HomeViewModel = koinViewModel()
            val savedDarkMode by homeViewModel.isDarkMode.collectAsState()
            val isLoggedIn by homeViewModel.isLoggedIn.collectAsState(initial = true)

            LaunchedEffect(isLoggedIn) {
                if (!isLoggedIn) {
                    val intent = Intent(this@HomeActivity, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            QuizAppTheme(
                darkTheme = savedDarkMode ?: isSystemInDarkTheme()
            ) {
                Scaffold { innerPadding ->
                    HomeNavigation(
                        viewModel = homeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
