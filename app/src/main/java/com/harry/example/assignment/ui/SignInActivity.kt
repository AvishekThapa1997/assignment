package com.harry.example.assignment.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.harry.example.assignment.R
import com.harry.example.assignment.database.entities.User
import com.harry.example.assignment.utility.*
import com.harry.example.assignment.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()
    private val utility: Utility by inject()
    private var rememberMeIsChecked = false
    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLoginResponse()
        setContentView(R.layout.activity_sign_in)
        sign_in.setOnClickListener {
            showProgress()
            obserValidationResponse()
            enableOrDisable(false)
            email = user_email.text.toString()
            password = user_password.text.toString()
            utility.checkLoginCredentials(email,password)
        }
        remember.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                rememberMeIsChecked = isChecked
        }
    }

    private fun observeLoginResponse() {
        userViewModel.user.observe(this, Observer {
            if (it != null) {
                Log.i("TAG", "observeLoginResponse: ${it}")
                toHomeActivity(it)
            } else {
                showMessage(SOMETHING_WENT_WRONG)
                enableOrDisable(true)
                hideProgress()
            }
        })
    }

    private fun showMessage(messaage: String) {
        Snackbar.make(findViewById<View>(android.R.id.content), messaage, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
    }

    private fun enableOrDisable(res: Boolean) {
        group.referencedIds.forEach {
            findViewById<View>(it).isEnabled = res
        }
    }

    private fun saveCredentials() {
        val sharedPreferences = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)
        editor.apply()
    }

    private fun toHomeActivity(user: User) {
        if(rememberMeIsChecked) {
            saveCredentials()
        }
        Log.i("TAG", "toHomeActivity: ")
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra(EMAIL, user.email)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivity(intent)
        finishAffinity()
    }

    private fun obserValidationResponse() {
        utility.loginCredentials.observe(this, Observer {
            when (it) {
                EMAIL_CANNOT_BE_EMPTY -> {
                    input_layout_mail.error = EMAIL_CANNOT_BE_EMPTY
                    enableOrDisable(true)
                    hideProgress()
                }
                INVALID_MAIL_ID -> {
                    input_layout_mail.error = INVALID_MAIL_ID
                    enableOrDisable(true)
                    hideProgress()
                }
                PASSWORD_CANNOT_BE_EMPTY -> {
                    input_layout_password.error = PASSWORD_CANNOT_BE_EMPTY
                    enableOrDisable(true)
                    hideProgress()
                }
                VALID_CREDENTIALS-> {
                    userViewModel.signInUser(email,password)
                }
            }
        })
    }
}