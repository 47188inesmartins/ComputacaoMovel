package com.example.myapplication.data

import com.example.myapplication.network.PictureApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val pictureRepository: PictureRepository
}

class DefaultAppContainer() : AppContainer {

    private val BASE_URL = "https://picsum.photos/"
    override val pictureRepository: PictureRepository by lazy {
        DefaultPicturesRepository(retrofitService)
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }
}