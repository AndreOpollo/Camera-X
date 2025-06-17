package com.example.camera_x.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState


@Composable
fun AdvancedControls(
    uiState: CameraUiState,
    onEvent:(CameraUiEvent)->Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f)
        )
    ){
        Column(modifier = Modifier.padding(16.dp)){
            if(uiState.maxZoom>uiState.minZoom){
                Text(text="Zoom", color = Color.White)
                Slider(
                    value = uiState.zoomRatio,
                    onValueChange = {onEvent(CameraUiEvent.ChangeZoom(it))},
                    valueRange = uiState.minZoom..uiState.maxZoom,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if(uiState.maxExposure>uiState.minExposure){
                Text(text = "Exposure", color = Color.White)
                Slider(
                    value = uiState.exposureIndex.toFloat(),
                    onValueChange = {onEvent(CameraUiEvent.ChangeExposure(it.toInt()))},
                    valueRange = uiState.minExposure.toFloat()..uiState.maxExposure.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            CaptureModeSelector(
                currentMode = uiState.captureMode,
                onModeChange = {onEvent(CameraUiEvent.ChangeCaptureMode(it))}
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }

}