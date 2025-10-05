package com.example.quizapp.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.quizapp.tools.uriToFile
import java.io.File

@Composable
fun AddQuestionScreen(
    onSubmit: (
        question: String,
        options: List<String>,
        correctIndex: Int,
        level: Int,
        imageFile: File?
    ) -> Unit
) {
    var questionText by remember { mutableStateOf("") }
    val options = remember { mutableStateListOf("", "", "", "") }
    var correctAnswerIndex by remember { mutableIntStateOf(0) }
    var level by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val imageFile = imageUri?.let {
        uriToFile(uri = it, context = context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add New Question", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            "Options (Select correct one using radio)",
            style = MaterialTheme.typography.labelLarge
        )

        repeat(4) { index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = correctAnswerIndex == index,
                    onClick = { correctAnswerIndex = index }
                )
                OutlinedTextField(
                    value = options[index],
                    onValueChange = { options[index] = it },
                    label = { Text("Option ${'A' + index}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }

        OutlinedTextField(
            value = level,
            onValueChange = { level = it.filter { ch -> ch.isDigit() } },
            label = { Text("Level") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { launcher.launch("image/*") }) {
//            Icon(Icons.Default.Person, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Select Image (Optional)")
        }

        imageUri?.let {
            Text(
                "Image selected: ${it.lastPathSegment}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        ElevatedButton(
            onClick = {
                if (questionText.isNotBlank() &&
                    options.all { it.isNotBlank() } &&
                    level.isNotBlank()
                ) {
                    onSubmit(
                        questionText,
                        options.toList(),
                        correctAnswerIndex,
                        level.toInt(),
                        imageFile
                    )
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()
        ) {
            Text("Submit Question")
        }
    }
}
