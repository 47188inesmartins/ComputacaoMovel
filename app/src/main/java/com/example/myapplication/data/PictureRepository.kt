package com.example.myapplication.data

import com.example.myapplication.model.Picture
import com.example.myapplication.network.PictureApiService

interface PictureRepository{

    suspend fun getPicture(): List<Picture>
}

class DefaultPicturesRepository(
    private val picturesApiService: PictureApiService
) : PictureRepository {
    override suspend fun getPicture(): List<Picture> =
        picturesApiService.getPicture()
}