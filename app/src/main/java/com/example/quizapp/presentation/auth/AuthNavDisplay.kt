package com.example.quizapp.presentation.auth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.quizapp.R
import com.example.quizapp.presentation.auth.screens.SignInScreen
import com.example.quizapp.presentation.auth.screens.SignUpScreen
import kotlinx.serialization.Serializable

@Serializable
data object SignIn : NavKey

@Serializable
data object SignUp : NavKey

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AuthNavigation(modifier: Modifier, authViewModel: AuthViewModel) {
    val backStack = remember { mutableStateListOf<Any>(SignIn) }
    val context = LocalContext.current

    LaunchedEffect(authViewModel.uiState) {
        if (authViewModel.uiState is AuthUiState.Error) {
            Toast.makeText(context, (authViewModel.uiState as AuthUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = { key ->
                when (key) {
                    is SignIn -> NavEntry(key) {
                        SignInScreen(
                            signInClick = { email, password ->
                                authViewModel.signIn(email, password)
                            },
                            onSignUpClick = {
                                if (backStack.last() !is SignUp) {
                                    backStack.add(SignUp)
                                }
                            }
                        )
                    }

                    is SignUp -> NavEntry(key) {
                        SignUpScreen(
                            onSignUpClick = { email, password ->
                                authViewModel.signUp(email, password)
                            },
                            signInClick = {
                                if (backStack.size > 1 && backStack[backStack.size - 2] is SignIn) {
                                    backStack.removeLast()
                                } else if (backStack.last() !is SignIn) {
                                    backStack.add(SignIn)
                                }
                            }
                        )
                    }

                    else -> NavEntry(Unit) { Text(stringResource(R.string.unknown_route)) }
                }
            }
        )

        if (authViewModel.uiState is AuthUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .run {
                        // Optional: add a semi-transparent background if needed
                        // background(Color.Black.copy(alpha = 0.3f))
                        this
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
