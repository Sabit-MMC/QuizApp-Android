package com.example.quizapp.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.auth.LoginResponse
import com.example.quizapp.data.model.auth.RegisterResponse
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.data.repository.auth.AuthRepository
import com.example.quizapp.tools.DataStoreHelper
import com.example.quizapp.tools.LCE
import com.example.quizapp.tools.toErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _loginState: MutableStateFlow<LCE<Boolean>?> = MutableStateFlow(null)
    var loginState = _loginState.asStateFlow()

    private val _registerState: MutableStateFlow<LCE<RegisterResponse>?> =
        MutableStateFlow(null)
    var registerState = _registerState.asStateFlow()

    val loginUserIdTextField = mutableStateOf(TextFieldValue())
    val loginPasswordTextField = mutableStateOf(TextFieldValue())

    val registerUserIdTextField = mutableStateOf(TextFieldValue())
    val registerPasswordTextField = mutableStateOf(TextFieldValue())

    fun login(userRequest: UserRequest) {
        _loginState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.login(userRequest)
                if (response.isSuccessful) {
                    dataStoreHelper.saveCredentials(userRequest.userId, userRequest.password)
                    _loginState.value = LCE.content(response.body())
                } else {
                    _loginState.value = LCE.error(errorMessage = response.toErrorResponse())
                }
            } catch (e: Exception) {
                _loginState.value = LCE.error(errorMessage = e.localizedMessage ?: "")
            }
        }
    }

    fun register(userRequest: UserRequest) {
        _registerState.value = LCE.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.register(userRequest)
                if (response.isSuccessful) {
                    _registerState.value = LCE.content(response.body())
                } else {
                    _registerState.value =
                        LCE.error(errorMessage = response.toErrorResponse())

                }
            } catch (e: Exception) {
                _registerState.value = LCE.error(errorMessage = e.localizedMessage)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreHelper.clearCredentials()
        }
    }

    fun checkLogin(onLoggedIn: (userId: String) -> Unit) {
        viewModelScope.launch {
            dataStoreHelper.getUserId().collect { userId ->
                if (!userId.isNullOrBlank()) {
                    onLoggedIn(userId)
                }
            }
        }
    }

    fun clearRegisterState(){
        _registerState.value = null
    }

    fun clearLoginStaterState(){
        _loginState.value = LCE.content(null)
    }

    fun clearLoginTextField() {
        loginUserIdTextField.value = TextFieldValue("")
        loginPasswordTextField.value = TextFieldValue("")
    }

    fun clearRegisterTextField() {
        registerUserIdTextField.value = TextFieldValue("")
        registerPasswordTextField.value = TextFieldValue("")

    }
}