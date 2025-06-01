package com.example.quizapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.repository.quiz.QuizRepository
import com.example.quizapp.tools.LCE
import com.example.quizapp.tools.toErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(private val quizRepository: QuizRepository) :
    ViewModel() {

    private val _uploadState: MutableStateFlow<LCE<Question>?> =
        MutableStateFlow(null)
    var uploadState = _uploadState.asStateFlow()

    val imageFile = mutableStateOf(File(""))

    fun uploadQuiz(
        question: String,
        options: List<String>,
        correctAnswerIndex: Int,
        level: Int,
        imageFile: File?
    ) {
        _uploadState.value = LCE.loading()

        viewModelScope.launch(IO) {
            try {
                val questionObject = JSONObject().apply {
                    put("question", question)
                    put("options", JSONArray(options))
                    put("correctAnswerIndex", correctAnswerIndex)
                }

                val requestBodyMap = mutableMapOf<String, RequestBody>()
                requestBodyMap["question"] = questionObject.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                requestBodyMap["level"] = level.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // Optional image part
                val imagePart = imageFile?.let {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", it.name, requestFile)
                }

                val response = quizRepository.uploadQuestion(requestBodyMap, imagePart)

                if (response.isSuccessful) {
                    _uploadState.value = LCE.content(response.body())
                } else {
                    _uploadState.value = LCE.error(errorMessage = response.toErrorResponse())
                }
            } catch (e: Exception) {
                _uploadState.value =
                    LCE.error(errorMessage = e.localizedMessage ?: "Unexpected error")
            }
        }
    }

}