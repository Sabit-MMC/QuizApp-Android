package com.example.quizapp.presentation.quiz

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
import com.example.quizapp.presentation.AddQuestionViewModel
import com.example.quizapp.presentation.MainScreen
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.tools.DataStoreHelper
import com.example.quizapp.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuizActivity : ComponentActivity() {
    private val questionViewModel: QuestionViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val addQuestionViewModel: AddQuestionViewModel by viewModels()

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        MainScreen(
                            navHostController,
                            authViewModel = authViewModel,
                            questionViewModel = questionViewModel,
                            addQuestionViewModel = addQuestionViewModel,
                            dataStoreHelper = dataStoreHelper
                        )
                    }
                }
            }
        }
    }
}