package com.harry.example.assignment.repository

import com.harry.example.assignment.database.UserDatabase
import com.harry.example.assignment.database.entities.User
import com.harry.example.assignment.network.ApiClient
import com.harry.example.assignment.network.ApiResponse
import java.net.UnknownHostException

class AppRepository(private val userDatabase: UserDatabase,private val apiClient: ApiClient) {
    suspend fun signUpUser(user: User?) {
        userDatabase.getUserDao().insert(user)
    }

    suspend fun checkUser(email: String?, password: String?): User? {
        return userDatabase.getUserDao().getUser(email, password)
    }

    suspend fun signInUser(email: String?, password: String?): User? {
        return checkUser(email, password)
    }

    suspend fun getAllPhotos(): List<ApiResponse>? {
        try {
            val response = apiClient.getAllPhotos()
            return response.body()
        } catch (exception: UnknownHostException) {
            return null
        }
    }
}