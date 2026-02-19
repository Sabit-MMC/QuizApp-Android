package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.tools.DataStoreConfig
import com.example.quizapp.presentation.quiz.model.Category
import com.example.quizapp.presentation.quiz.model.Question
import com.example.quizapp.presentation.quiz.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val quizRepository: QuizRepository,
    private val dataStoreConfig: DataStoreConfig
) : ViewModel() {

    var uiState by mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    var isQuestionsLoading by mutableStateOf(false)
        private set

    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    // Quiz Results
    var correctAnswers by mutableIntStateOf(0)
    var wrongAnswers by mutableIntStateOf(0)
    var skippedAnswers by mutableIntStateOf(0)

    private val _navigationEvent = MutableSharedFlow<HomeNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val isDarkMode: StateFlow<Boolean?> = dataStoreConfig.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isLoggedIn: Flow<Boolean> = dataStoreConfig.isLoggedIn

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            when (val result = quizRepository.getCategories()) {
                is NetworkResult.Success -> {
                    uiState = HomeUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    uiState = HomeUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun startQuiz(categoryId: String, level: String) {
        viewModelScope.launch {
            isQuestionsLoading = true
            // Reset results
            correctAnswers = 0
            wrongAnswers = 0
            skippedAnswers = 0
            
            when (val result = quizRepository.getQuestions(categoryId, level)) {
                is NetworkResult.Success -> {
                    questions = result.data
                    _navigationEvent.emit(HomeNavigationEvent.NavigateToQuestion)
                }
                is NetworkResult.Error -> {
                    // Handle error
                }
                else -> {}
            }
            isQuestionsLoading = false
        }
    }

    fun recordAnswer(isCorrect: Boolean?) {
        when (isCorrect) {
            true -> correctAnswers++
            false -> wrongAnswers++
            null -> skippedAnswers++
        }
    }

    fun toggleDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            dataStoreConfig.setDarkMode(darkMode)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            dataStoreConfig.setLoggedIn(false)
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val categories: List<Category>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeNavigationEvent {
    data object NavigateToQuestion : HomeNavigationEvent
    data object NavigateToResult : HomeNavigationEvent
}
