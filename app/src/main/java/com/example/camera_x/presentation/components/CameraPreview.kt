package com.example.camera_x.presentation.components

import android.media.Image
import android.net.Uri
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState

@Composable
fun CameraPreview(
    uiState: CameraUiState,
    onEvent: (CameraUiEvent)-> Unit,
    onImageCaptured:(Uri)->Unit,
    onError:(Throwable)->Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null)}
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    val scaleGesture = remember {
        ScaleGestureDetector(context,
            object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scale = detector.scaleFactor
                    val newZoomRatio = (uiState.zoomRatio*scale)
                        .coerceIn(uiState.minZoom,uiState.maxZoom)
                    onEvent(CameraUiEvent.ChangeZoom(newZoomRatio))
                    return true
                }
            })
    }

    LaunchedEffect(uiState.lensFacing,
        uiState.flashMode,
        uiState.captureMode) {
        try {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            val preview = Preview.Builder()
                .build()
            imageCapture = createImageCapture(
                flashMode = uiState.flashMode,
                captureMode = uiState.captureMode
            )
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(uiState.lensFacing)
                .build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            previewView?.let { preview.setSurfaceProvider(it.surfaceProvider) }

            camera?.let {
                cam->
                val zoomState = cam.cameraInfo.zoomState.value
                val exposureState = cam.cameraInfo.exposureState
                onEvent(CameraUiEvent.UpdateCameraInfo(
                    minZoom = zoomState?.minZoomRatio?:1f,
                    maxZoom = zoomState?.maxZoomRatio?:1f,
                    currentZoom = zoomState?.zoomRatio?:1f,
                    minExposure = exposureState.exposureCompensationRange.lower,
                    maxExposure = exposureState.exposureCompensationRange.upper,
                    currentExposure = exposureState.exposureCompensationIndex
                ))
            }

        }catch (e: Exception){
            onError(e)
        }
    }
    LaunchedEffect(uiState.countdownValue,uiState.countdownActive) {
        if(uiState.countdownActive && uiState.countdownValue == 0){
            imageCapture?.let {
                capture->
                //TODO
            }
        }
    }

}

private fun createImageCapture(
    flashMode:Int,
    captureMode:Int = ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
): ImageCapture{
    return ImageCapture.Builder()
        .setFlashMode(flashMode)
        .setCaptureMode(captureMode)
        .setJpegQuality(95)
        .build()
}