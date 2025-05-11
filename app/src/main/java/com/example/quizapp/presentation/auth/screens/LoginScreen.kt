package com.example.quizapp.presentation.auth.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.presentation.quiz.QuizActivity
import com.example.quizapp.tools.Status

@Composable
fun LoginScreen(navHostController: NavHostController,authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val loginState by authViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = authViewModel.userIdTextField.value,
            onValueChange = { newValue ->
                authViewModel.userIdTextField.value = newValue
            }, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = authViewModel.passwordTextField.value,
            onValueChange = { newValue ->
                authViewModel.passwordTextField.value = newValue
            }, modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            authViewModel.login(
                userRequest = UserRequest(
                    userId = authViewModel.userIdTextField.value.text,
                    password = authViewModel.passwordTextField.value.text
                )
            )
        }, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {

            when(loginState?.status){
                Status.LOADING -> {
                    CircularProgressIndicator(modifier = Modifier, color = MaterialTheme.colorScheme.background)
                }
                Status.CONTENT -> {
                    Text("Daxil ol",Modifier.padding(vertical = 8.dp), fontSize = 18.sp)
                   val intent = Intent(context, QuizActivity::class.java)
                    context.startActivity(intent)
                }
                Status.ERROR -> {
                    Text("Daxil ol",Modifier.padding(vertical = 8.dp), fontSize = 18.sp)
                    Toast.makeText(context, "Error occur", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Text("Daxil ol", modifier = Modifier.padding(vertical = 8.dp), fontSize = 18.sp)
                }
            }

        }

    }

}