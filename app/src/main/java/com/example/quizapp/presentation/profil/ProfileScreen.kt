package com.example.quizapp.presentation.profil

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quizapp.presentation.auth.MainActivity
import com.example.quizapp.presentation.auth.AuthViewModel
import com.example.quizapp.tools.DataStoreHelper

@Composable
fun ProfileScreen(viewModel: AuthViewModel,dataStoreHelper: DataStoreHelper) {
    val context = LocalContext.current
    var userId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dataStoreHelper.getUserId().collect {_userId ->
            if (!_userId.isNullOrBlank()) {
                userId = _userId
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("İstifadəçi: $userId", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            viewModel.logout()
            (context as Activity)
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            context.finish()
        }) {
            Text("Çıxış")
        }
    }
}