package com.example.camera_x.presentation.components


import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ImageFilter(
    val name:String,
    val matrix: ColorMatrix,
    val previewColor: Color)

fun getAvailableFilters(): List<ImageFilter> = listOf(
    ImageFilter("Original", ColorMatrix().apply { setSaturation(1f) }, Color.White),
    ImageFilter("GrayScale", ColorMatrix().apply { setSaturation(0f) }, Color.LightGray),
    ImageFilter("Sepia", ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f,     0f,     0f,     1f, 0f
        )
    ), Color(0xFF704214)),

    ImageFilter("Invert", ColorMatrix(
        floatArrayOf(
            -1f,  0f,  0f,  0f, 255f,
            0f, -1f,  0f,  0f, 255f,
            0f,  0f, -1f,  0f, 255f,
            0f,  0f,  0f,  1f,   0f
        )
    ), Color.Black),

    ImageFilter("Bright", ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 50f,
            0f, 1f, 0f, 0f, 50f,
            0f, 0f, 1f, 0f, 50f,
            0f, 0f, 0f, 1f, 0f
        )
    ), Color.Yellow),

    ImageFilter("Contrast", ColorMatrix(
        floatArrayOf(
            1.5f, 0f, 0f, 0f, 128 * (1 - 1.5f),
            0f, 1.5f, 0f, 0f, 128 * (1 - 1.5f),
            0f, 0f, 1.5f, 0f, 128 * (1 - 1.5f),
            0f, 0f, 0f, 1f, 0f
        )
    ), Color.DarkGray),

    ImageFilter("Warm", ColorMatrix(
        floatArrayOf(
            1.1f, 0f, 0f, 0f, 0f,
            0f, 1.05f, 0f, 0f, 0f,
            0f, 0f, 0.9f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ), Color(0xFFFF7043)),

    ImageFilter("Cool", ColorMatrix(
        floatArrayOf(
            0.9f, 0f, 0f, 0f, 0f,
            0f, 0.95f, 0f, 0f, 0f,
            0f, 0f, 1.1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ), Color(0xFF40C4FF)),

    ImageFilter("Vintage", ColorMatrix(
        floatArrayOf(
            0.6f, 0.3f, 0.1f, 0f, 0f,
            0.2f, 0.7f, 0.1f, 0f, 0f,
            0.1f, 0.2f, 0.7f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ), Color(0xFFBDB76B)),

    ImageFilter("Polaroid", ColorMatrix(
        floatArrayOf(
            1.483f, -0.122f, -0.016f, 0f, -5.31f,
            -0.843f, 1.613f, -0.135f, 0f, 2.78f,
            -0.012f, 0.073f, 1.843f, 0f, -12.08f,
            0f, 0f, 0f, 1f, 0f
        )
    ), Color(0xFF8A2BE2))
)

@Composable
fun FilterItem(
    filter: ImageFilter,
    isSelected: Boolean,
    onClick:()->Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable{onClick()}
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(filter.previewColor)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = filter.name,
            fontSize = 10.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp)
        )
    }

}

fun applyColorMatrixFilter(original: Bitmap, matrix: ColorMatrix): Bitmap {
    val filtered = createBitmap(original.width, original.height, original.config!!)
    val canvas = Canvas(filtered)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(matrix)
    canvas.drawBitmap(original, 0f, 0f, paint)
    return filtered
}
