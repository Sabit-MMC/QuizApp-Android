package com.example.quizapp.presentation.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : ComponentActivity() {
    val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizAppTheme {
                Scaffold {
                    AuthNavigation(modifier = Modifier.padding(it),authViewModel = authViewModel)
                }
            }
        }
    }
}