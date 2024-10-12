package com.example.myapplication.network

import com.example.myapplication.model.Picture
import retrofit2.http.GET

interface PictureApiService {
    @GET("v2/list")
    suspend fun getPicture(): List<Picture>
}