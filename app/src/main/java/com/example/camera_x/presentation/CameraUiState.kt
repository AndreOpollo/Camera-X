package com.example.camera_x.presentation

import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView

data class CameraUiState(
    val isInitialized: Boolean = false,
    val capturedUri: Uri? = null,
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val flashMode:Int = ImageCapture.FLASH_MODE_OFF,
    val captureMode: Int = ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY,
    val showControls: Boolean = true,
    val showGrid: Boolean = false,
    val showAdvancedControls:Boolean = false,
    val zoomRatio: Float = 1f,
    val minZoom: Float = 1f,
    val maxZoom: Float = 1f,
    val exposureIndex: Int = 0,
    val minExposure: Int = 0,
    val maxExposure: Int = 0,
    val timerSeconds: Int = 0,
    val countdownActive:Boolean = false,
    val countdownValue:Int = 0,
    val error: String? = null,
    val isLoading: Boolean = false,
    val camera: Camera? = null,
    val imageCapture: ImageCapture? = null,
    val previewView: PreviewView? = null,
    val shouldCapturePhoto:Boolean = false,
    val isThumbnailClicked:Boolean = false
)