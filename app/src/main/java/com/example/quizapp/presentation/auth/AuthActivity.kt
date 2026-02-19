package com.example.quizapp.presentation.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.quizapp.presentation.quiz.HomeActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val savedDarkMode by authViewModel.isDarkMode.collectAsState()
            
            LaunchedEffect(authViewModel.navigationEvent) {
                authViewModel.navigationEvent.collect { event ->
                    when (event) {
                        is AuthNavigationEvent.NavigateToHome -> {
                            startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
                            finish()
                        }
                        is AuthNavigationEvent.NavigateToSignIn -> {
                            Toast.makeText(context, "Registration successful! Please login.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            QuizAppTheme(
                darkTheme = savedDarkMode ?: isSystemInDarkTheme()
            ) {
                Scaffold {
                    AuthNavigation(modifier = Modifier.padding(it), authViewModel = authViewModel)
                }
            }
        }
    }
}
