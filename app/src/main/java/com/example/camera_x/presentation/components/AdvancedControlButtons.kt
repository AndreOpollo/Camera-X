package com.example.camera_x.presentation.components

import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CaptureModeSelector(
    currentMode:Int,
    onModeChange:(Int)->Unit
){
    Text(text="Quality", color = Color.White)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        val modes = listOf(
            ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY to "Quality",
            ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY to "Speed"
        )
        modes.forEach {
                (mode,label)->
            FilterChip(
                onClick = {onModeChange(mode)},
                label = {Text(text = label)},
                selected = currentMode == mode
            )
        }
    }
}