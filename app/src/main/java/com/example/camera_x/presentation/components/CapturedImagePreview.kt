package com.example.camera_x.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.camera_x.presentation.utils.saveBitmapToCache
import com.example.camera_x.presentation.utils.saveImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun CapturedImagePreview(
    uri: Uri,
    onShare:(Uri)->Unit,
    onDelete:()->Unit,
    onBackClicked:()->Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val filters = remember {getAvailableFilters()}
    var selectedFilter by remember { mutableStateOf(filters.first()) }
    var filteredBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri, selectedFilter) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        filteredBitmap = applyColorMatrixFilter(originalBitmap, selectedFilter.matrix)
    }
    Box(modifier = modifier.fillMaxSize()){
        filteredBitmap?.let{bitmap->
            Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            IconButton(
                onClick = {
                    filteredBitmap?.let {
                        val filteredUri = saveBitmapToCache(context, it)
                        onShare(filteredUri)
                    }
                },
                modifier = Modifier
                    .size(40.dp),
                enabled = filteredBitmap!=null
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ){
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(filters){
                    filter->
                    FilterItem(
                        filter = filter,
                        isSelected = selectedFilter == filter,
                        onClick = {selectedFilter = filter}
                    )

                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete")
                }
                Button(onClick = {
                    filteredBitmap?.let {
                        saveImage(context,it)
                    }
                }) {
                    Icon(Icons.Default.Save, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save")
                }
            }
        }
    }
}