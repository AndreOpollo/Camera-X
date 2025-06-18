package com.example.camera_x.presentation.components

import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState
import com.example.camera_x.presentation.CameraViewModel
import com.example.camera_x.presentation.utils.createImageCapture
import com.example.camera_x.presentation.utils.setupGestureHandling
import com.example.camera_x.presentation.utils.takePhoto

@Composable
fun CameraPreview(
    uiState: CameraUiState,
    onEvent: (CameraUiEvent)-> Unit,
    onImageCaptured:(Uri)->Unit,
    onError:(Throwable)->Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val scaleGestureDetector =
        ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scale = detector.scaleFactor
                    val newZoomRatio = (uiState.zoomRatio * scale)
                        .coerceIn(uiState.minZoom, uiState.maxZoom)
                    onEvent(CameraUiEvent.ChangeZoom(newZoomRatio))
                    return true
                }
            })

    LaunchedEffect(uiState.lensFacing,
        uiState.flashMode,
        uiState.captureMode,
        uiState.previewView,
        ) {

        try {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            val preview = Preview.Builder()
                .build()
            val imageCapture = createImageCapture(
                flashMode = uiState.flashMode,
                captureMode = uiState.captureMode
            )
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(uiState.lensFacing)
                .build()
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            uiState.previewView?.let { preview.setSurfaceProvider(it.surfaceProvider) }
            onEvent(CameraUiEvent.SetCamera(camera))
            onEvent(CameraUiEvent.SetImageCapture(imageCapture))

                val zoomState = camera.cameraInfo.zoomState.value
                val exposureState = camera.cameraInfo.exposureState
                onEvent(CameraUiEvent.UpdateCameraInfo(
                    minZoom = zoomState?.minZoomRatio?:1f,
                    maxZoom = zoomState?.maxZoomRatio?:1f,
                    currentZoom = zoomState?.zoomRatio?:1f,
                    minExposure = exposureState.exposureCompensationRange.lower,
                    maxExposure = exposureState.exposureCompensationRange.upper,
                    currentExposure = exposureState.exposureCompensationIndex
                ))

        }catch (e: Exception){
            onError(e)
        }
    }
    LaunchedEffect(uiState.countdownValue,uiState.countdownActive) {
        if(uiState.countdownActive && uiState.countdownValue == 0){
            uiState.imageCapture?.let {
                capture->
                takePhoto(
                    context = context,
                    imageCapture = capture,
                    onImageCapture = onImageCaptured,
                    onError = onError
                )
            }
        }
    }
    LaunchedEffect(uiState.shouldCapturePhoto) {
        if(uiState.shouldCapturePhoto) {
            uiState.imageCapture?.let { capture ->
                takePhoto(
                    context = context,
                    imageCapture = capture,
                    onImageCapture = onImageCaptured,
                    onError = onError
                )
            }
            onEvent(CameraUiEvent.ResetCaptureFlag)
        }
    }


    Box(modifier = Modifier.fillMaxSize()){
        AndroidView(
            factory = {ctx->
                PreviewView(ctx).also{
                    onEvent(CameraUiEvent.SetPreviewView(it))


                }
            },
            modifier = Modifier.fillMaxSize()
        ){
            view->
            setupGestureHandling(
                previewView = view,
                context = context,
                camera = uiState.camera,
                scaleGestureDetector = scaleGestureDetector,
                onEvent = onEvent
            )
        }
        CameraOverlays(
            uiState = uiState,
            onEvent = onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}

