package com.example.camera_x.presentation.components

import androidx.camera.core.CaptureBundles
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    showAdvanced:Boolean,
    onToggle:()-> Unit
){
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if(showAdvanced)Icons.Default.KeyboardArrowUp
            else
                Icons.Default.KeyboardArrowDown,
            contentDescription = "Settings",
            tint = Color.White)
    }
}
@Composable
fun FlashButton(
    flashMode:Int,
    onFlashModeChange:()->Unit
){
    IconButton(onClick = onFlashModeChange) {
        val icon = when(flashMode){
            ImageCapture.FLASH_MODE_ON ->Icons.Default.FlashOn
            ImageCapture.FLASH_MODE_AUTO->Icons.Default.FlashAuto
            else->Icons.Default.FlashOff
        }
        Icon(imageVector = icon,
            contentDescription = "Flash",
            tint = Color.White)
    }
}
@Composable
fun TimerButton(
    timerSeconds:Int,
    onTimerChange:()->Unit
){
    IconButton(onClick = onTimerChange) {
        if(timerSeconds>0){
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ){
                Text(
                    text = timerSeconds.toString(),
                    modifier = Modifier.padding(4.dp),
                    color = Color.Black
                )
            }
        } else {
            Icon(imageVector = Icons.Default.Timer,
                contentDescription = "Timer",
                tint = Color.White)
        }
    }
}

@Composable
fun GridButton(
    showGrid:Boolean,
    onToggle: () -> Unit
){
    IconButton(onClick = onToggle) {
        Icon(imageVector = Icons.Default.GridOn,
            contentDescription = "Grid",
            tint = if(showGrid)
                Color.Yellow
            else
                Color.White)
    }
}

@Composable
fun SwitchCameraButton(
    onSwitch:()->Unit
){
    IconButton(onClick = onSwitch) {
        Icon(imageVector = Icons.Default.Cameraswitch,
            contentDescription = "Switch Camera",
            tint = Color.White)
    }
}
@Composable
fun CaptureButton(
    modifier: Modifier = Modifier,
    onCapture: ()->Unit
){
    FloatingActionButton(onClick = onCapture,
        modifier = modifier.size(72.dp),
        containerColor = Color.White) {
        Icon(imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = "Capture",
            modifier = Modifier.size(32.dp),
            tint = Color.Black)
    }
}