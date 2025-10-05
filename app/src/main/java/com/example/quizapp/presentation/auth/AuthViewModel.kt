package com.example.quizapp.presentation.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.auth.RegisterResponse
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.data.repository.auth.AuthRepository
import com.example.quizapp.tools.DataStoreHelper
import com.example.quizapp.tools.LCE
import com.example.quizapp.tools.toErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.forEach

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


    private val _otpFields = mutableStateListOf<OtpField>()
    val otpFields: List<OtpField> get() = _otpFields

    private val _otp = mutableStateOf("")
    val otp: MutableState<String> = _otp

    private val _timeLeft = mutableIntStateOf(10) // countdown in seconds
    val timeLeft: State<Int> = _timeLeft

    val formattedTime: State<String> = derivedStateOf {
        val minutes = _timeLeft.intValue / 60
        val seconds = _timeLeft.intValue % 60
        String.format(Locale.US,"%02d:%02d", minutes, seconds)
    }

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value--
            }
        }
    }

    fun resetTimer(newTimeInSeconds: Int = 10) {
        _timeLeft.value = newTimeInSeconds
        startTimer()
    }

    fun handleOtpInputChange(
        index: Int,
        count: Int,
        newValue: String,
        otpFieldsValues: List<MutableState<OtpField>>,
        otp: MutableState<String>
    ) {
        if (newValue.length <= 1) {
            otpFieldsValues[index].value = otpFieldsValues[index].value.copy(text = newValue)
        } else if (newValue.length == 2) {
            otpFieldsValues[index].value =
                otpFieldsValues[index].value.copy(text = newValue.lastOrNull()?.toString() ?: "")
        } else if (newValue.isNotEmpty()) {
            newValue.forEachIndexed { i, char ->
                if (index + i < count) {
                    otpFieldsValues[index + i].value =
                        otpFieldsValues[index + i].value.copy(text = char.toString())
                }
            }
        }

        try {
            if (newValue.isEmpty() && index > 0) {
                otpFieldsValues.getOrNull(index - 1)?.value?.focusRequester?.requestFocus()
            } else if (index < count - 1 && newValue.isNotEmpty() && newValue.length <= 1) {
                otpFieldsValues.getOrNull(index + 1)?.value?.focusRequester?.requestFocus()
            } else if (newValue.length > 1) {
                // Focus the last field after pasting
                val lastFilledIndex = minOf(index + newValue.length - 1, count - 1)
                otpFieldsValues.getOrNull(lastFilledIndex)?.value?.focusRequester?.requestFocus()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        otp.value = otpFieldsValues.joinToString("") { it.value.text }

    }

    fun focusNextBox(
        index: Int,
        count: Int,
        otpFieldsValues: List<MutableState<OtpField>>
    ) {
        if (index + 1 < count) {
            try {
                otpFieldsValues[index + 1].value.focusRequester?.requestFocus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
 data class OtpField(
    val text: String,
    val index: Int,
    val focusRequester: FocusRequester? = null
)
