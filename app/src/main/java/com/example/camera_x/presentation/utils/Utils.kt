package com.example.camera_x.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.camera_x.presentation.CameraUiEvent


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
}