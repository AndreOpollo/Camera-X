package com.example.camera_x.presentation

import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView

sealed class CameraUiEvent{
    data object InitializeCamera: CameraUiEvent()
    data object CapturePhoto: CameraUiEvent()
    data object ToggleControls: CameraUiEvent()
    data object ToggleGrid: CameraUiEvent()
    data object ToggleAdvancedControls: CameraUiEvent()
    data object SwitchCameraLens: CameraUiEvent()
    data object SwitchFlashMode: CameraUiEvent()
    data object TimerSwitch: CameraUiEvent()
    data object StartCountdown: CameraUiEvent()
    data object CountdownTick: CameraUiEvent()
    data object ShareImage: CameraUiEvent()
    data object DeleteImage: CameraUiEvent()
    data object ClearError: CameraUiEvent()
    data class ChangeCaptureMode(val mode: Int): CameraUiEvent()
    data class ChangeZoom(val zoom: Float): CameraUiEvent()
    data class ChangeZoomByGestures(val zoom: Float): CameraUiEvent()
    data class ChangeExposure(val exposure: Int): CameraUiEvent()
    data class FocusAt(val x: Float, val y: Float): CameraUiEvent()
    data class UpdateCameraInfo(
        val minZoom: Float,
        val maxZoom: Float,
        val currentZoom: Float,
        val minExposure: Int,
        val maxExposure: Int,
        val currentExposure: Int
    ): CameraUiEvent()
    data class SetCamera(val camera: Camera): CameraUiEvent()
    data class SetImageCapture(val imageCapture: ImageCapture): CameraUiEvent()
    data class SetPreviewView(val previewView: PreviewView): CameraUiEvent()
    data object ResetCaptureFlag: CameraUiEvent()
    data object ThumbnailClicked: CameraUiEvent()
}