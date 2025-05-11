package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.AllLevelsQuestions
import com.example.quizapp.model.LevelQuestionGroup
import com.example.quizapp.model.QuizResult
import com.example.quizapp.model.QuizSubmissionRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val quizRepository: QuizRepository) :
    ViewModel() {

    private val _questionState: MutableStateFlow<LCE<AllLevelsQuestions>?> = MutableStateFlow(null)
    var questionState = _questionState.asStateFlow()

    private val _leveledQuestionState: MutableStateFlow<LCE<LevelQuestionGroup>?> =
        MutableStateFlow(null)
    var leveledQuestionState = _leveledQuestionState.asStateFlow()

    private val _submitQuizState: MutableStateFlow<LCE<QuizResult>?> =
        MutableStateFlow(null)
    var submitQuizState = _submitQuizState.asStateFlow()

    private val _insertQuizState: MutableStateFlow<LCE<QuizResult>?> =
        MutableStateFlow(null)
    var insertQuiz = _insertQuizState.asStateFlow()

    private val _getAllResultState: MutableStateFlow<LCE<List<QuizResult>>?> =
        MutableStateFlow(null)
    var getAllResultState = _getAllResultState.asStateFlow()

    var listOfSubmitQuizAnswers = mutableListOf<Int>()
    lateinit var quizResult: QuizResult


    var selectedOption by mutableIntStateOf(-1)
        private set

    var currentIndex by mutableIntStateOf(0)

    fun onOptionSelected(index: Int) {
        selectedOption = index
    }

    fun clearSelectedOption() {
        selectedOption = -1
    }


    fun getQuestions() {
        _questionState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getQuestions()
            if (response.isSuccessful) {
                _questionState.value = LCE.content(response.body())
            } else {
                _questionState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun getLeveledQuestion(level: Int) {
        _leveledQuestionState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getLeveledQuestion(level)
            if (response.isSuccessful) {
                _leveledQuestionState.value = LCE.content(response.body())
            } else {
                _leveledQuestionState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun submitQuiz(quizSubmissionRequest: QuizSubmissionRequest) {
        _submitQuizState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.submitQuiz(quizSubmissionRequest)
            if (response.isSuccessful) {
                _submitQuizState.value = LCE.content(response.body())
            } else {
                _submitQuizState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun insertQuiz(quizResult: QuizResult) {
        _insertQuizState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.insertQuiz(quizResult)
            if (response.isSuccessful) {
                _insertQuizState.value = LCE.content(response.body())
            } else {
                _insertQuizState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun fetchAllResults() {
        _getAllResultState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.fetchAllResults()
            if (response.isSuccessful) {
                _getAllResultState.value = LCE.content(response.body())
            } else {
                _getAllResultState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun clearViewModel() {
        _submitQuizState.value = null
        selectedOption = -1
        listOfSubmitQuizAnswers.clear()
        currentIndex = 0
    }
    fun clearSubmitState(){
        _submitQuizState.value = LCE.content(null)
    }
}