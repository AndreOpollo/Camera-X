package com.example.camera_x.presentation.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    error:String,
    onDismiss:()->Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Camera Error")},
        text = {Text(error)},
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text="OK")
            }
        }

    )
}