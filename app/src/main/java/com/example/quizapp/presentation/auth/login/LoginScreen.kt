package com.example.quizapp.presentation.auth.login

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.presentation.auth.AuthNavHostObject
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.presentation.quiz.QuizActivity
import com.example.quizapp.tools.Status

@Composable
fun LoginScreen(navHostController: NavHostController,authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val loginState by authViewModel.loginState.collectAsState()

    BackHandler(enabled = true) {
        (context as Activity)
        context.finish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Daxil ol",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = authViewModel.userIdTextField.value,
            onValueChange = { authViewModel.userIdTextField.value = it },
            label = { Text("İstifadəçi ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = authViewModel.passwordTextField.value,
            onValueChange = { authViewModel.passwordTextField.value = it },
            label = { Text("Şifrə") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.login(
                    userRequest = UserRequest(
                        userId = authViewModel.userIdTextField.value.text,
                        password = authViewModel.passwordTextField.value.text
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            when (loginState?.status) {
                Status.LOADING -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp),
                        strokeWidth = 2.dp
                    )
                }

                Status.CONTENT -> {
                    Text("Daxil ol", fontSize = 18.sp)
                    authViewModel.clearTextFields()
                    (context as Activity).startActivity(
                        Intent(context, QuizActivity::class.java)
                    )
                    context.finish()
                }

                Status.ERROR -> {
                    Text("Yenidən cəhd edin", fontSize = 18.sp)
                    Toast.makeText(context, "Xəta baş verdi", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Text("Daxil ol", fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navHostController.navigate(AuthNavHostObject.RegisterScreen)
            }
        ) {
            Text("Hesabınız yoxdur? Qeydiyyatdan keçin")
        }
    }
}
