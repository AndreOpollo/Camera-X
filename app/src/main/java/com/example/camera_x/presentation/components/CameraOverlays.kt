package com.example.camera_x.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState


@Composable
fun CameraOverlays(
    uiState: CameraUiState,
    onEvent:(CameraUiEvent)->Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        if(uiState.countdownActive && uiState.countdownValue>0){
            CountdownOverlay(
                countdownValue = uiState.countdownValue,
                modifier = Modifier.fillMaxSize()
            )
        }
        if(uiState.showGrid){
            GridOverlay(modifier = Modifier.fillMaxSize())
        }
        if(uiState.zoomRatio>1.1f){
            ZoomIndicator(
                zoomRatio = uiState.zoomRatio,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            )
        }
        if(uiState.showControls){
            CameraControls(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

}