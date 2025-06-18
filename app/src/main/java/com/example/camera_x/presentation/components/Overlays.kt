package com.example.camera_x.presentation.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GridOverlay(modifier: Modifier = Modifier){
    Canvas(
        modifier = modifier
    ){
        val height = size.height
        val width = size.width

        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(width/3,0f),
            end = Offset(width/3,height),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(2*width/3,0f),
            end = Offset(2*width/3,height),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(0f,height/3),
            end = Offset(width,height/3),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(0f,2*height/3),
            end = Offset(width,2*height/3),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun ZoomIndicator(
    zoomRatio: Float,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f)
        )
    ){
        Text(
            text = "${String.format("%.1f", zoomRatio)}x",
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CountdownOverlay(
    countdownValue:Int,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier
        .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center){
        Text(
            text = countdownValue.toString(),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}