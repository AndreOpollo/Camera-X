package com.example.camera_x.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState

@Composable
fun CameraControls(
    uiState: CameraUiState,
    onEvent:(CameraUiEvent)->Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(uiState.showAdvancedControls){
            AdvancedControls(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier.fillMaxWidth()
            )
        }
        MainControls(
            uiState = uiState,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CaptureButton(
            onCapture = {onEvent(CameraUiEvent.CapturePhoto)}
        )
    }
}