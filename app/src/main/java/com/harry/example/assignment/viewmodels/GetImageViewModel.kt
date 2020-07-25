package com.harry.example.assignment.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harry.example.assignment.MyApplication
import com.harry.example.assignment.network.ApiResponse
import com.harry.example.assignment.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetImageViewModel(
    myApplication: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(myApplication) {
    var apiResponses: LiveData<List<ApiResponse>?> = MutableLiveData()
    fun getAllPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
           val response = appRepository.getAllPhotos()
            (apiResponses as MutableLiveData<List<ApiResponse>?>).postValue(response)
        }
    }
}