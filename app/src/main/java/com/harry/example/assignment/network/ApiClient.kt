package com.harry.example.assignment.network

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {

    @GET("/photos")
    suspend fun getAllPhotos(): Response<List<ApiResponse>?>
}