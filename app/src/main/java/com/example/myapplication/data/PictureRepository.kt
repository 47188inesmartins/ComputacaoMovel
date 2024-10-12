package com.example.myapplication.data

import android.util.Log
import com.example.myapplication.model.Picture
import com.example.myapplication.network.PictureApiService

interface PictureRepository{

    suspend fun getPicture(): List<Picture>
}

class DefaultPicturesRepository(
    private val picturesApiService: PictureApiService
) : PictureRepository {
    override suspend fun getPicture(): List<Picture> {
        val pictures = picturesApiService.getPicture()
        Log.i("Ines", pictures[0].toString())
        return pictures
    }
}