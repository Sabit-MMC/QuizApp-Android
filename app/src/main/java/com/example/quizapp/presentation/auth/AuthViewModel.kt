package com.example.quizapp.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.data.repository.auth.AuthRepository
import com.example.quizapp.tools.LCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState: MutableStateFlow<LCE<Map<String, Boolean>>?> = MutableStateFlow(null)
    var loginState = _loginState.asStateFlow()

    private val _registerState: MutableStateFlow<LCE<Map<String, Boolean>>?> =
        MutableStateFlow(null)
    var registerState = _registerState.asStateFlow()

    val userIdTextField = mutableStateOf(TextFieldValue())
    val passwordTextField = mutableStateOf(TextFieldValue())

    fun login(userRequest: UserRequest) {
        _loginState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.login(userRequest)
            if (response.isSuccessful) {
                delay(2000)
                _loginState.value = LCE.content(response.body())
            } else {
                _loginState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }

    fun register(userRequest: UserRequest) {
        _registerState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.register(userRequest)
            if (response.isSuccessful) {
                _registerState.value = LCE.content(response.body())
            } else {
                _registerState.value =
                    LCE.error(errorMessage = response.errorBody()?.string() ?: "")
            }
        }
    }
}