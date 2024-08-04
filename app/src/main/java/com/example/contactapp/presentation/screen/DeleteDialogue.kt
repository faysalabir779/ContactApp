package com.example.contactapp.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DeleteDialogue(
    isOpen: Boolean,
    label: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    if (isOpen){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = "Delete")
            },
            text = {
                Text(text = label)
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Delete")
                }
            }
        )
    }
}