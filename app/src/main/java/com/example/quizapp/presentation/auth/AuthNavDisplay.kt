package com.example.quizapp.presentation.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
                        signInClick = { _, _ ->
                            // Handle sign in logic
                        },
                        onSignUpClick = {
                            if (backStack.last() !is SignUp) {
                                backStack.add(SignUp)
                            }
                        },
                        onForgotPasswordClick = {
                            // Handle forgot password
                        }
                    )
                }

                is SignUp -> NavEntry(key) {
                    SignUpScreen(
                        onSignUpClick = { _, _, _ ->
                            // Handle sign up logic
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
}
