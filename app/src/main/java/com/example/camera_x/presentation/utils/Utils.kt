package com.example.camera_x.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.camera_x.presentation.CameraUiEvent
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun createImageCapture(
    flashMode:Int,
    captureMode: Int = ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
): ImageCapture{
    return ImageCapture.Builder()
        .setCaptureMode(captureMode)
        .setFlashMode(flashMode)
        .setJpegQuality(95)
        .build()
}
fun setupGestureHandling(
    previewView: PreviewView,
    context: Context,
    camera: Camera?,
    scaleGestureDetector: ScaleGestureDetector,
    onEvent:(CameraUiEvent)->Unit
){
    val gestureDetector = GestureDetector(context,
        object: GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                onEvent(CameraUiEvent.FocusAt(e.x,e.y))

                val factory = previewView.meteringPointFactory
                val point = factory.createPoint(e.x,e.y)
                val action = FocusMeteringAction.Builder(point)
                    .addPoint(point, FocusMeteringAction.FLAG_AF)
                    .addPoint(point, FocusMeteringAction.FLAG_AE)
                    .setAutoCancelDuration(3, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                camera?.cameraControl?.startFocusAndMetering(action)
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                onEvent(CameraUiEvent.ToggleControls)
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
    })

    previewView.setOnTouchListener {
        v,event->
        Log.d("GestureDebug", "Touch event: ${event.action}")

        var handled = false
        if(scaleGestureDetector.onTouchEvent(event)){
            handled = true
        }
        if(!scaleGestureDetector.isInProgress){
            if(gestureDetector.onTouchEvent(event)){
                handled = true
                v.performClick()
            }
        }
        handled
    }
}

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCapture: (Uri)-> Unit,
    onError: (Throwable)-> Unit
){
    val now = System.currentTimeMillis()
    val name = "IMG_${now}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, name)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images")
        }
    }
    val outputOptions = ImageCapture
        .OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
    ).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                if(savedUri!=null){
                    onImageCapture(savedUri)
                } else {
                    onError(Throwable("Image is null"))
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }

        }
    )
}

fun shareImage(context: Context,uri: Uri){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM,uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent,"Share Image"))
}
fun deleteImage(context: Context,uri:Uri){
    context.contentResolver.delete(uri,null,null)
    Toast.makeText(context, "Image deleted from gallery", Toast.LENGTH_SHORT).show()

}
fun saveImage(context: Context, bitmap: Bitmap){
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilteredImages")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        try {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Unable to create image uri", Toast.LENGTH_SHORT).show()
    }
}
fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "filtered_images")
    cachePath.mkdirs()

    val file = File(cachePath, "filtered_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}