package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repository.quiz.QuizRepository
import com.example.quizapp.tools.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val dataStoreHelper: DataStoreHelper
) :
    ViewModel() {

    val userId = mutableStateOf("")

    private val _timeLeft = MutableStateFlow(15)
    val timeLeft: StateFlow<Int> = _timeLeft

    private var timerJob: Job? = null

    fun startTimer(duration: Int = 15, level: Int) {
        timerJob?.cancel()
        _timeLeft.value = duration

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value = _timeLeft.value - 1
            }

            _timeLeft.value = duration
            startTimer(duration, level)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}