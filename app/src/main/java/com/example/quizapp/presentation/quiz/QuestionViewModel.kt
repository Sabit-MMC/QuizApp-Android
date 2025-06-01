package com.example.quizapp.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.Category
import com.example.quizapp.data.model.LevelQuestionGroup
import com.example.quizapp.data.model.LevelStatus
import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.model.QuizSubmissionRequest
import com.example.quizapp.data.repository.quiz.QuizRepository
import com.example.quizapp.tools.DataStoreHelper
import com.example.quizapp.tools.LCE
import com.example.quizapp.tools.toErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val dataStoreHelper: DataStoreHelper
) :
    ViewModel() {

    val userId = mutableStateOf("")

    private val _levelStatusState: MutableStateFlow<LCE<List<LevelStatus>>?> =
        MutableStateFlow(null)
    var levelStatusState = _levelStatusState.asStateFlow()

    private val _categoriesState: MutableStateFlow<LCE<List<Category>>?> =
        MutableStateFlow(null)
    var categoriesState = _categoriesState.asStateFlow()

    private val _leveledQuestionState: MutableStateFlow<LCE<LevelQuestionGroup>?> =
        MutableStateFlow(null)
    var leveledQuestionState = _leveledQuestionState.asStateFlow()

    private val _submitQuizState: MutableStateFlow<LCE<QuizResult>?> =
        MutableStateFlow(null)
    var submitQuizState = _submitQuizState.asStateFlow()

    private val _getAllResultState: MutableStateFlow<LCE<List<QuizResult>>?> =
        MutableStateFlow(null)
    var getAllResultState = _getAllResultState.asStateFlow()

    private val _timeLeft = MutableStateFlow(15)
    val timeLeft: StateFlow<Int> = _timeLeft

    var listOfSubmitQuizAnswers = mutableListOf<Int>()
    lateinit var quizResult: QuizResult

    private var timerJob: Job? = null

    var selectedOption by mutableIntStateOf(-1)
        private set

    var currentIndex by mutableIntStateOf(0)

    fun onOptionSelected(index: Int) {
        selectedOption = index
    }

    fun clearSelectedOption() {
        selectedOption = -1
    }

    fun getLevels() {
        _levelStatusState.value = LCE.Companion.loading()
        viewModelScope.launch(IO) {
            try {
                dataStoreHelper.getUserId().collect {
                    if (!it.isNullOrEmpty()) {
                        userId.value = it
                        val response = quizRepository.getLevels(it)

                        if (response.isSuccessful && response.body() != null) {
                            _levelStatusState.value = LCE.content(response.body())
                        } else {
                            _levelStatusState.value =
                                LCE.error(errorMessage = response.errorBody()?.string() ?: "")
                        }
                    }
                }
            } catch (e: Exception) {
                _levelStatusState.value =
                    LCE.error(errorMessage = e.localizedMessage)
            }
        }
    }

    fun getLeveledQuestion(level: Int) {
        _leveledQuestionState.value = LCE.Companion.loading()
        viewModelScope.launch(IO) {
            val response = quizRepository.getLeveledQuestion(level)
            if (response.isSuccessful && response.body() != null) {
                _leveledQuestionState.value = LCE.Companion.content(response.body())
            } else {
                _leveledQuestionState.value =
                    LCE.Companion.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun submitResult(quizSubmissionRequest: QuizSubmissionRequest) {
        _submitQuizState.value = LCE.Companion.loading()
        viewModelScope.launch(IO) {
            try {
                val response = quizRepository.insertResult(quizSubmissionRequest)
                if (response.isSuccessful && response.body() != null) {
                    _submitQuizState.value = LCE.Companion.content(response.body())
                } else {
                    _submitQuizState.value =
                        LCE.Companion.error(errorMessage = response.toErrorResponse())
                }
            } catch (e: Exception) {
                _submitQuizState.value =
                    LCE.Companion.error(errorMessage = e.localizedMessage)
            }
        }
    }

    fun onSubmitOrNext(level: Int, isLastQuestion: Boolean) {
        if (!isLastQuestion) {
            listOfSubmitQuizAnswers.add(selectedOption)
            currentIndex += 1
            clearSelectedOption()
            stopTimer()
        } else {
            listOfSubmitQuizAnswers.add(selectedOption)
            submitResult(
                QuizSubmissionRequest(
                    userId = userId.value,
                    level = level,
                    answers = listOfSubmitQuizAnswers
                )
            )
            stopTimer()
        }
    }

    fun clearViewModel() {
        _submitQuizState.value = null
        selectedOption = -1
        listOfSubmitQuizAnswers.clear()
        currentIndex = 0
    }

    fun clearSubmitState() {
        _submitQuizState.value = LCE.Companion.content(null)
    }

    fun startTimer(duration: Int = 15, level: Int) {
        timerJob?.cancel()
        _timeLeft.value = duration

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value = _timeLeft.value - 1
            }

            onSubmitOrNext(
                level,
                currentIndex == leveledQuestionState.value?.data?.questions?.lastIndex
            )
            _timeLeft.value = duration
            startTimer(duration, level)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}