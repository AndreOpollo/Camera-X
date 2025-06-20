package com.example.camera_x.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.camera_x.presentation.CameraUiEvent
import com.example.camera_x.presentation.CameraUiState
import com.example.camera_x.presentation.utils.Routes

@Composable
fun CameraControls(
    uiState: CameraUiState,
    onEvent:(CameraUiEvent)->Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.capturedUri != null) {
               ThumbnailButton(
                   onClick = {
                       navController.navigate(
                           Routes.PreviewScreen.createRoute(uiState.capturedUri.toString())
                       )
                   },
                   uri = uiState.capturedUri
               )
            } else {
                Spacer(modifier = Modifier
                    .padding(start = 8.dp)
                    .size(64.dp))
            }
            CaptureButton(
                onCapture = { onEvent(CameraUiEvent.CapturePhoto) }
            )
            Spacer(modifier = Modifier
                .padding(end = 8.dp)
                .size(64.dp))
        }

    }
}