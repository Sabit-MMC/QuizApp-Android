package com.example.quizapp.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.data.model.auth.UserRequest
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.tools.Status
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(navHostController: NavHostController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val registerState by authViewModel.registerState.collectAsState()

    LaunchedEffect(registerState?.status) {
        if (registerState?.status == Status.CONTENT) {
            Toast.makeText(context, registerState?.data?.message, Toast.LENGTH_SHORT).show()
            authViewModel.clearRegisterTextField()
            delay(1000L)
            authViewModel.clearRegisterState()
            navHostController.navigateUp()
        }
    }

    LaunchedEffect(registerState?.status) {
        if (registerState?.status == Status.ERROR) {
            Toast.makeText(context, registerState?.errorMessage ?: "Xəta baş verdi", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hesab yarat",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = authViewModel.registerUserIdTextField.value,
            onValueChange = { newValue ->
                authViewModel.registerUserIdTextField.value = newValue
            },
            label = { Text("İstifadəçi ID") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = authViewModel.registerPasswordTextField.value,
            onValueChange = { newValue ->
                authViewModel.registerPasswordTextField.value = newValue
            },
            label = { Text("Şifrə") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.register(
                    userRequest = UserRequest(
                        userId = authViewModel.registerUserIdTextField.value.text,
                        password = authViewModel.registerPasswordTextField.value.text
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            if (registerState?.status == Status.LOADING) {
                CircularProgressIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                )
            } else {
                Text("Qeydiyyatdan keç", Modifier.padding(vertical = 8.dp), fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                authViewModel.clearRegisterTextField()
                authViewModel.clearRegisterState()
                navHostController.navigateUp()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Hesabınız var? Daxil olun")
        }
    }
}
