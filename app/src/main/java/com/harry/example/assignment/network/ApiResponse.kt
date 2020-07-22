package com.harry.example.assignment.network

import com.google.gson.annotations.SerializedName

data class ApiResponse(@SerializedName("id") val id : Int, @SerializedName("title")val title : String, @SerializedName("url")val imageUrl: String)