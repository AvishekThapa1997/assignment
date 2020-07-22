package com.harry.example.assignment.utility

import android.content.Context
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class Utility {
    var validationResponse: LiveData<String> = MutableLiveData()
    var loginCredentials: LiveData<String> = MutableLiveData()
    var logoutResposne: LiveData<String> = MutableLiveData()
    fun validateCredentials(
        name: String?,
        email: String?,
        password: String?,
        confirmpassword: String?
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            if (name.isNullOrEmpty() || name.isNullOrBlank()) {
                setResponse(NAME_CANNOT_BE_EMPTY)
                return@launch
            }
            if (!name.matches(Regex("^[a-zA-Z ]*\$"))) {
                setResponse(NAME_SHOULD_CONTAIN_ALPHABETS_ONLY)
                return@launch
            }
            if (email.isNullOrBlank() || email.isNullOrEmpty()) {
                setResponse(EMAIL_CANNOT_BE_EMPTY)
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setResponse(INVALID_MAIL_ID)
                return@launch
            }
            if (password.isNullOrEmpty() || password.isNullOrBlank()) {
                setResponse(PASSWORD_CANNOT_BE_EMPTY)
                return@launch
            }
            if (password != confirmpassword) {
                setResponse(PASSWORD_SHOULD_BE_SAME)
                return@launch
            }
            setResponse(VALID_CREDENTIALS)
        }
    }

    private fun setResponse(response: String) {
        (validationResponse as MutableLiveData<String>).postValue(response)
    }
    private fun setLoginCredentialsResponse(response: String) {
        (loginCredentials as MutableLiveData<String>).postValue(response)
    }
    fun logoutUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        (logoutResposne as MutableLiveData<String>).value = USER_LOGOUT
    }
     fun checkLoginCredentials(email: String?,password: String?) {
        CoroutineScope(Dispatchers.Default).launch {
            if(email.isNullOrEmpty() || email.isNullOrEmpty()){
                setLoginCredentialsResponse(EMAIL_CANNOT_BE_EMPTY)
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setLoginCredentialsResponse(INVALID_MAIL_ID)
                return@launch
            }
            if (password.isNullOrEmpty() || password.isNullOrBlank()) {
                setLoginCredentialsResponse(PASSWORD_CANNOT_BE_EMPTY)
                return@launch
            }
            setLoginCredentialsResponse(VALID_CREDENTIALS)
        }

    }
}