package com.example.camera_x.presentation.components


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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap

data class ImageFilter(val name:String,val matrix: ColorMatrix)

fun getAvailableFilters(): List<ImageFilter> = listOf(
    ImageFilter("Original", ColorMatrix().apply { setSaturation(1f) }),
    ImageFilter("GrayScale", ColorMatrix().apply { setSaturation(0f) }),
    ImageFilter("Sepia", ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f,     0f,     0f,     1f, 0f
        )
    )
    ),
    ImageFilter("Invert", ColorMatrix(
        floatArrayOf(
            -1f,  0f,  0f,  0f, 255f,
            0f, -1f,  0f,  0f, 255f,
            0f,  0f, -1f,  0f, 255f,
            0f,  0f,  0f,  1f,   0f
        )
    )),
    ImageFilter("Bright +", ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 50f,
            0f, 1f, 0f, 0f, 50f,
            0f, 0f, 1f, 0f, 50f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    ImageFilter("Contrast +", ColorMatrix(
        floatArrayOf(
            1.5f, 0f, 0f, 0f, 128 * (1 - 1.5f),
            0f, 1.5f, 0f, 0f, 128 * (1 - 1.5f),
            0f, 0f, 1.5f, 0f, 128 * (1 - 1.5f),
            0f, 0f, 0f, 1f, 0f
        )
    ))

)
@Composable
fun CapturedImagePreview2(
    uri: Uri,
    onShare: (Uri) -> Unit,
    onDelete: () -> Unit,
) {
    val context = LocalContext.current
    val filters = remember { getAvailableFilters() }
    var selectedFilter by remember { mutableStateOf(filters.first()) }
    var filteredBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri, selectedFilter) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        filteredBitmap = applyColorMatrixFilter(originalBitmap, selectedFilter.matrix)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            filteredBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Filter selection row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(filters) { filter ->
                Button(
                    onClick = { selectedFilter = filter },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = filter.name)
                }
            }
        }

        // Optional: Share/Delete actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                filteredBitmap?.let {
                    val filteredUri = saveBitmapToCache(context, it)
                    onShare(filteredUri)
                }
            }) { Text("Share") }
            Button(onClick = onDelete) { Text("Delete") }
        }
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
