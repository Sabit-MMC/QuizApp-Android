package com.example.quizapp.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.auth.LoginResponse
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.data.repository.auth.AuthRepository
import com.example.quizapp.tools.DataStoreHelper
import com.example.quizapp.tools.LCE
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

    private val _loginState: MutableStateFlow<LCE<LoginResponse>?> = MutableStateFlow(null)
    var loginState = _loginState.asStateFlow()

    private val _registerState: MutableStateFlow<LCE<Map<String, Boolean>>?> =
        MutableStateFlow(null)
    var registerState = _registerState.asStateFlow()

    val userIdTextField = mutableStateOf(TextFieldValue())
    val passwordTextField = mutableStateOf(TextFieldValue())

    fun login(userRequest: UserRequest) {
        _loginState.value = LCE.Companion.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.login(userRequest)
            if (response.isSuccessful) {
                dataStoreHelper.clearAccessToken()
                dataStoreHelper.clearRefreshToken()
                dataStoreHelper.saveAccessToken(response.body()?.accessToken ?: "")
                dataStoreHelper.saveRefreshToken(response.body()?.refreshToken ?: "")
                dataStoreHelper.saveCredentials(userRequest.userId, userRequest.password)
                _loginState.value = LCE.Companion.content(response.body())
            } else {
                _loginState.value =
                    LCE.Companion.error(errorMessage = response.errorBody()?.string() ?: "")
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
                if (response.code() == 409) {
                    _registerState.value =
                        LCE.error(errorMessage = "Bu userId artıq mövcuddur. Zəhmət olmasa başqa birini seçin.")
                } else {
                    _registerState.value =
                        LCE.error(errorMessage = response.errorBody()?.string() ?: "")
                }
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

    fun clearTextFields() {
        userIdTextField.value = TextFieldValue("")
        passwordTextField.value = TextFieldValue("")
    }
}