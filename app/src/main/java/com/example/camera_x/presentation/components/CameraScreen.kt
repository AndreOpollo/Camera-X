package com.example.camera_x.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraViewModel
import com.example.camera_x.presentation.utils.deleteImage
import com.example.camera_x.presentation.utils.shareImage

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel<CameraViewModel>(),
    onImageCaptured:(Uri)->Unit = {},
    onError:(Throwable)->Unit = {}
){
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        Manifest.permission.CAMERA
    }

    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        ){
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        } else {
        viewModel.onEvent(CameraUiEvent.InitializeCamera)
        }
    }
    Box(modifier = modifier.fillMaxSize()){
        if(uiState.value.capturedUri == null){
            CameraPreview(
                uiState = uiState.value,
                onEvent = viewModel::onEvent,
                onImageCaptured = {uri->
                    viewModel.onImageCaptured(uri)
                    onImageCaptured(uri)
                },
                onError = {
                    error->
                    viewModel.onError(error)
                    onError(error)
                }
            )
        } else {
            CapturedImagePreview(
                uri = uiState.value.capturedUri!!,
                onShare = {
                    shareImage(
                        context = context,
                        uri = uiState.value.capturedUri!!
                    )
                },
                onDelete = {
                    deleteImage(
                        context = context,
                        uri = uiState.value.capturedUri!!
                    )
                }
            )
        }

        uiState.value.error?.let {
            error->
            ErrorDialog(
                error = error,
                onDismiss = {viewModel.onEvent(CameraUiEvent.ClearError)}
            )
        }
    }

}