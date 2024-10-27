package com.example.myapplication.network

import com.example.myapplication.model.MarsPhoto
import com.example.myapplication.model.Picture
import retrofit2.http.GET

interface MarsApiService {
    /**
     * Returns a [List] of [MarsPhoto] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}
