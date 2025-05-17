package com.example.quizapp.tools.components

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun BackConfirmHandler(
    onConfirmExit: () -> Unit,
    dialogTitle: String = "Diqqət",
    dialogText: String = "Əminsinizmi? Geri çıxmaqla bütün məlumatlar silinəcək.",
) {
    var showDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle, color = MaterialTheme.colorScheme.error) },
            text = { Text(dialogText) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onConfirmExit()
                }) {
                    Text("Bəli")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Xeyr")
                }
            }
        )
    }
}