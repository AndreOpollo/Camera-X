package com.example.camera_x.presentation.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.camera_x.presentation.CameraViewModel
import com.example.camera_x.presentation.components.CapturedImagePreview
import com.example.camera_x.presentation.utils.deleteImage
import com.example.camera_x.presentation.utils.shareImage
import androidx.core.net.toUri

@Composable
fun CapturedImagePreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel<CameraViewModel>(),
    navController: NavHostController,
    imageUri: String
){
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val uri = imageUri.toUri()


    Box(modifier = modifier.fillMaxSize()){
        CapturedImagePreview(
            uri = uri,
            onShare = {
                shareImage(
                    context = context,
                    uri = uri
                )
            },
            onDelete = {
                deleteImage(
                    context = context,
                    uri = uri
                )
            },
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }

}