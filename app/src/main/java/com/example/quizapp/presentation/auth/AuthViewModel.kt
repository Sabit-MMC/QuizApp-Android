package com.example.quizapp.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.network.NetworkResult
import com.example.quizapp.data.tools.DataStoreConfig
import com.example.quizapp.presentation.auth.model.AuthRequestBody
import com.example.quizapp.presentation.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStoreConfig: DataStoreConfig
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    private val _navigationEvent = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val isDarkMode: StateFlow<Boolean?> = dataStoreConfig.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val requestBody = AuthRequestBody(userId = email, password = pass)
            when (val result = authRepository.signIn(requestBody)) {
                is NetworkResult.Success -> {
                    dataStoreConfig.setUserId(result.data.userId)
                    dataStoreConfig.setLoggedIn(true)
                    uiState = AuthUiState.Success
                    _navigationEvent.emit(AuthNavigationEvent.NavigateToHome)
                }
                is NetworkResult.Error -> {
                    uiState = AuthUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun signUp(email: String, pass: String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val requestBody = AuthRequestBody(userId = email, password = pass)
            when (val result = authRepository.signUp(requestBody)) {
                is NetworkResult.Success -> {
                    uiState = AuthUiState.Success
                    _navigationEvent.emit(AuthNavigationEvent.NavigateToSignIn)
                }
                is NetworkResult.Error -> {
                    uiState = AuthUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }
}

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

sealed interface AuthNavigationEvent {
    data object NavigateToHome : AuthNavigationEvent
    data object NavigateToSignIn : AuthNavigationEvent
}
