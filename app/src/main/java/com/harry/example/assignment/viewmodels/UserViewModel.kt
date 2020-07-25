package com.harry.example.assignment.viewmodels

import android.app.Application
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harry.example.assignment.database.entities.User
import com.harry.example.assignment.network.ApiResponse
import com.harry.example.assignment.repository.AppRepository
import com.harry.example.assignment.utility.EMAIL_ALREADY_EXISTS
import com.harry.example.assignment.utility.USER_REGISTERATION_FAILED
import com.harry.example.assignment.utility.USER_REGISTERED_SUCCESSFULLY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UserViewModel(
    mApplication: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(mApplication) {
    var signUpResponse: LiveData<String> = MutableLiveData()
    var loggingIn: LiveData<Boolean> = MutableLiveData()
    var user: LiveData<User?> = MutableLiveData()
    fun signUpUser(user: User?) {
        viewModelScope.launch(Dispatchers.Default) {
            user.let {
                if (appRepository.checkUser(user?.email, user?.password) == null) {
                    try {
                        appRepository.signUpUser(user)
                        (signUpResponse as MutableLiveData<String>).postValue(
                            USER_REGISTERED_SUCCESSFULLY
                        )
                    } catch (exception: Exception) {
                        (signUpResponse as MutableLiveData<String>).postValue(
                            USER_REGISTERATION_FAILED
                        )
                    }
                } else {
                    (signUpResponse as MutableLiveData<String>).postValue(
                        EMAIL_ALREADY_EXISTS
                    )
                }
                (loggingIn as MutableLiveData<Boolean>).postValue(false)
            }
        }
    }

    fun signInUser(email: String?, password: String?) {
        viewModelScope.launch(Dispatchers.Default) {
            val resposne = appRepository.signInUser(email, password)
            (user as MutableLiveData<User?>).postValue(resposne)
        }
    }


}