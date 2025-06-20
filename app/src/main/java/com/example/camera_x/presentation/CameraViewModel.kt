package com.example.camera_x.presentation


import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camera_x.presentation.utils.takePhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()



    fun onEvent(e: CameraUiEvent){
        when(e){
            CameraUiEvent.CapturePhoto -> capturePhoto()
            is CameraUiEvent.ChangeCaptureMode -> changeCaptureMode(e.mode)
            is CameraUiEvent.ChangeExposure -> changeExposure(e.exposure)
            is CameraUiEvent.ChangeZoom -> changeZoom(e.zoom)
            CameraUiEvent.ClearError -> clearError()
            CameraUiEvent.CountdownTick -> countdownTick()
            CameraUiEvent.DeleteImage -> deleteImage()
            is CameraUiEvent.FocusAt -> {}
            CameraUiEvent.InitializeCamera -> initializeCamera()
            CameraUiEvent.ShareImage -> {}
            CameraUiEvent.StartCountdown -> startCountDown()
            CameraUiEvent.SwitchCameraLens -> switchCamera()
            CameraUiEvent.SwitchFlashMode -> changeFlashMode()
            CameraUiEvent.TimerSwitch -> switchTimer()
            CameraUiEvent.ToggleAdvancedControls -> toggleAdvancedControls()
            CameraUiEvent.ToggleControls -> toggleControls()
            CameraUiEvent.ToggleGrid -> toggleGrid()
            is CameraUiEvent.UpdateCameraInfo -> updateCameraInfo(
                minZoom = e.minZoom,
                maxZoom = e.maxZoom,
                currentZoom = e.currentZoom,
                minExposure = e.minExposure,
                maxExposure = e.maxExposure,
                currentExposure = e.currentExposure
            )

            is CameraUiEvent.SetCamera -> setCamera(e.camera)
            is CameraUiEvent.SetImageCapture -> setImageCapture(e.imageCapture)
            is CameraUiEvent.SetPreviewView -> setPreviewView(e.previewView)
            CameraUiEvent.ResetCaptureFlag -> resetCaptureFlag()
            is CameraUiEvent.ChangeZoomByGestures -> changeZoomByGestures(e.zoom)
            CameraUiEvent.ThumbnailClicked -> thumbnailClicked()
        }
    }
    private fun initializeCamera(){
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        _uiState.update { it.copy(isInitialized = true, isLoading = false) }
    }
    private fun capturePhoto(){
        val currentState = _uiState.value
        if(currentState.timerSeconds>0){
            _uiState.update {
                it.copy(
                    countdownValue = currentState.timerSeconds,
                    countdownActive = true
                )
            }
            startCountdownTimer()

        } else {
            currentState.imageCapture?.let {
                capture->
                _uiState.update {
                    it.copy(countdownActive = false, countdownValue = 0, shouldCapturePhoto = true)
                }

            }

        }
    }
    private fun startCountdownTimer(){
        viewModelScope.launch {
            while (_uiState.value.countdownActive && _uiState.value.countdownValue>0){
                delay(1000)
                _uiState.update {
                    it.copy(countdownValue = it.countdownValue - 1)
                }
            }
            if(_uiState.value.countdownActive && _uiState.value.countdownValue == 0){
                _uiState.update {
                    it.copy(countdownActive = false, shouldCapturePhoto = true)
                }
            }
        }
    }
    private fun toggleControls(){
        _uiState.update {
            it.copy(showControls = !it.showControls)
        }
    }
    private fun toggleGrid(){
        _uiState.update {
            it.copy(showGrid = !it.showGrid)
        }
    }
    private fun toggleAdvancedControls(){
        _uiState.update {
            it.copy(showAdvancedControls = !it.showAdvancedControls)
        }
    }
    private fun focusAt(){}
    private fun updateCameraInfo(
        minZoom: Float,
        maxZoom:Float,
        currentZoom:Float,
        minExposure:Int,
        maxExposure:Int,
        currentExposure:Int
    ){
        _uiState.update {
            it.copy(
                minZoom = minZoom,
                maxZoom = maxZoom,
                zoomRatio = currentZoom,
                exposureIndex = currentExposure,
                minExposure = minExposure,
                maxExposure = maxExposure
            )
        }
    }
    private fun switchCamera(){
        val newLensFacing = if(_uiState.value.lensFacing == CameraSelector.LENS_FACING_BACK){
            CameraSelector.LENS_FACING_FRONT
        }else{
            CameraSelector.LENS_FACING_BACK
        }
        _uiState.update {
            it.copy(
                lensFacing = newLensFacing
            )
        }
    }
    private fun changeFlashMode(){
        val nextMode = when(_uiState.value.flashMode){
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_AUTO
            ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_ON
            else -> ImageCapture.FLASH_MODE_OFF
        }
        _uiState.update {
            it.copy(
                flashMode = nextMode
            )
        }
    }
    private fun switchTimer(){
        val newTimer = when(_uiState.value.timerSeconds){
            0->3
            3->10
            else->0
        }
        _uiState.update {
            it.copy(timerSeconds = newTimer)
        }
    }
    private fun startCountDown(){
        _uiState.update {
            it.copy(
                countdownActive = true,
                countdownValue = it.timerSeconds
            )
        }
    }
    private fun countdownTick(){
        _uiState.update {
            it.copy(
                countdownValue = maxOf(0,it.countdownValue -1)
            )
        }
    }
    private fun deleteImage(){
        _uiState.update {
            it.copy(
                capturedUri = null
            )
        }
    }
    private fun clearError(){
        _uiState.update {
            it.copy(error = null)
        }
    }
    private fun changeCaptureMode(mode:Int){
        _uiState.update {
            it.copy(captureMode = mode)
        }
    }
    private fun changeZoom(zoom:Float){
        val constrainedZoom = zoom.coerceIn(_uiState.value.minZoom,
            _uiState.value.maxZoom)
        if (constrainedZoom != _uiState.value.zoomRatio) {
            _uiState.update {
                it.copy(zoomRatio = constrainedZoom)
            }

        }

        _uiState.value.camera?.cameraControl?.setZoomRatio(constrainedZoom)
    }
    private fun changeZoomByGestures(zoom: Float){
        _uiState.update {
            it.copy(zoomRatio = zoom)
        }
    }
    private fun resetCaptureFlag(){
        _uiState.update {
            it.copy(shouldCapturePhoto = false)
        }
    }
    private fun changeExposure(exposure:Int){
        val constrainedExposure = exposure.coerceIn(
            _uiState.value.minExposure,
            _uiState.value.maxExposure
        )
        _uiState.update {
            it.copy(exposureIndex = constrainedExposure)
        }
        _uiState.value.camera?.cameraControl?.setExposureCompensationIndex(constrainedExposure)

    }
    private fun thumbnailClicked(){
        _uiState.update {
            it.copy(isThumbnailClicked = true)
        }
    }
    fun setCamera(camera: Camera){
        _uiState.update {
            it.copy(camera = camera)
        }
    }
    fun setImageCapture(imageCapture: ImageCapture){
        _uiState.update {
            it.copy(imageCapture = imageCapture)
        }
    }
    fun setPreviewView(previewView: PreviewView){
        _uiState.update {
            it.copy(previewView = previewView)
        }
    }
    fun onImageCaptured(uri: Uri){
        _uiState.update {
            it.copy(capturedUri = uri)
        }
    }
    fun onError(error: Throwable){
        _uiState.update {
            it.copy(
                error = error.message,
                isLoading = false
            )
        }
    }

}