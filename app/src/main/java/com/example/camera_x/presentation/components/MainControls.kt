package com.example.camera_x.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState


@Composable
fun MainControls(
    uiState: CameraUiState,
    onEvent: (CameraUiEvent)-> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
    SettingsButton(
        showAdvanced = uiState.showAdvancedControls,
        onToggle ={ onEvent(CameraUiEvent.ToggleAdvancedControls)}
    )
    FlashButton(
        flashMode = uiState.flashMode,
        onFlashModeChange = {onEvent(CameraUiEvent.SwitchFlashMode)}
    )
    TimerButton(
        timerSeconds = uiState.timerSeconds,
        onTimerChange = {onEvent(CameraUiEvent.TimerSwitch)}
    )
    GridButton(
        showGrid = uiState.showGrid,
        onToggle = {onEvent(CameraUiEvent.ToggleGrid)}
    )
    SwitchCameraButton(
        onSwitch = {onEvent(CameraUiEvent.SwitchCameraLens)}
    )
    }
}