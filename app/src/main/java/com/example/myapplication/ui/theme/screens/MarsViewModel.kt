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
import com.example.myapplication.MarsPhotosApplication
import com.example.myapplication.data.MarsPhotosRepository
import com.example.myapplication.data.PictureRepository
import com.example.myapplication.model.MarsPhoto
import com.example.myapplication.model.PhotosRolls
import com.example.myapplication.model.Picture
import com.example.myapplication.network.FireBase
import com.google.firebase.Firebase
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
data class MarsUiState (
    val photos: List<MarsPhoto> = emptyList(),
    val currentPhoto: MarsPhoto? = null
)

data class PicturesUiState(
    val picturesList: List<Picture> = emptyList(),
    val currentPicture: Picture? = null
)

data class Rolls(
    val rolls:Int = 0
)


class MarsViewModel(
    private val marsPhotosRepository: MarsPhotosRepository,
    private val picturesRepository: PictureRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState())
    var picturesUiState: PicturesUiState by  mutableStateOf(PicturesUiState())
    var rollsUiState: Rolls by  mutableStateOf(Rolls())
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
        getPictures()
        rollsUiState = rollsUiState.copy(rolls = 0)
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    private fun getMarsPhotos() {
        viewModelScope.launch {
            val getPics = marsPhotosRepository.getMarsPhotos()
            marsUiState = MarsUiState(
                getPics,
                getPics.random()
            )
        }
    }

    fun rollPictureMars() {
        val newRandomPicture = marsUiState.photos.random()
        marsUiState = marsUiState.copy(currentPhoto = newRandomPicture)
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

    fun saveImage(){
        if(marsUiState.currentPhoto!= null && picturesUiState.currentPicture!=null ){
            FireBase().savePhotos(
                marsUiState.currentPhoto!!,
                picturesUiState.currentPicture!!,
                rollsUiState.rolls
            )
        }
    }

    suspend fun read(): PhotosRolls? {
        if(marsUiState.currentPhoto!= null && picturesUiState.currentPicture!=null ) {
            val read =  FireBase().readPhoto(
                marsUiState.currentPhoto!!,
                picturesUiState.currentPicture!!,
                rollsUiState.rolls
            )
            if(read!=null){
                marsUiState = marsUiState.copy(currentPhoto = read.marsPhoto)
                picturesUiState = picturesUiState.copy(currentPicture = read.picture)
                rollsUiState = read.rolls.let { rollsUiState.copy(rolls = it) }
            }
        }
        return null
    }

    /**
     * Factory for [MarsViewModel] that takes [MarsPhotosRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                val picsPhotosRepository = application.container.pictureRepository

                MarsViewModel(
                    marsPhotosRepository = marsPhotosRepository,
                    picturesRepository = picsPhotosRepository
                )
            }
        }
    }
}
