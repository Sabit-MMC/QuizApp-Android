package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.AllLevelsQuestions
import com.example.quizapp.model.LevelQuestionGroup
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

    private val _leveledQuestionState: MutableStateFlow<LCE<LevelQuestionGroup>?> = MutableStateFlow(null)
    var leveledQuestionState = _leveledQuestionState.asStateFlow()

    var selectedOption by mutableStateOf(-1)
        private set

    fun onOptionSelected(index: Int) {
        selectedOption = index
    }

    fun clearSelectedOption(){
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

}