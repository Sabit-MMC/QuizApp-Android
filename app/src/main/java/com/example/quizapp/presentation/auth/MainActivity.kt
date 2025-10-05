package com.example.quizapp.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.quiz.QuizActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.checkLogin { userId ->
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }

        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            QuizAppTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.Companion
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        AuthNavHost(navHostController = navHostController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}





