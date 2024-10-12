package com.example.myapplication.ui.theme.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.data.PictureRepository
import com.example.myapplication.model.Picture
import com.example.myapplication.PictureApplication
import kotlinx.coroutines.launch

data class PicturesUiState(
    val picturesList: List<Picture> = emptyList(),
    val currentPicture: Picture? = null
)

class PicturesViewModel (
    private val picturesRepository: PictureRepository
): ViewModel(){

    var picturesUiState: PicturesUiState by  mutableStateOf(PicturesUiState())

    init {
        getPictures()
    }

    private fun getPictures() {
        viewModelScope.launch {
            val pictures = picturesRepository.getPicture()
            val randomPicture = pictures.random()
            picturesUiState = PicturesUiState(pictures, randomPicture)
        }
    }

    fun rollPicture() {
        val newRandomPicture = picturesUiState.picturesList.random()
        picturesUiState = picturesUiState.copy(currentPicture = newRandomPicture)
    }

    fun applyBlur() {
        picturesUiState.currentPicture?.let { current ->
            val blurredPicture = current.copy(
                download_url = "${current.download_url}?blur=4"
            )
            picturesUiState = picturesUiState.copy(currentPicture = blurredPicture)
        }
    }

    fun applyGrayScale() {
        picturesUiState.currentPicture?.let { current ->
            val grayScalePicture = current.copy(
                download_url = "${current.download_url}?grayscale"
            )
            picturesUiState = picturesUiState.copy(currentPicture = grayScalePicture)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY]
                        as PictureApplication)
                val repository = application.container.pictureRepository
                PicturesViewModel(picturesRepository = repository)
            }
        }
    }
}