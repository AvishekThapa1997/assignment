package com.harry.example.assignment.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.harry.example.assignment.R
import com.harry.example.assignment.utility.EMAIL
import com.harry.example.assignment.utility.PASSWORD
import com.harry.example.assignment.utility.SOMETHING_WENT_WRONG
import com.harry.example.assignment.utility.USER_CREDENTIALS
import com.harry.example.assignment.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.E

class SplashActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeLoginResponse()
        val animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim)
        welcome.animation = animation
        Thread {
            Thread.sleep(3000)
            val sharedPreferences = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
            if (sharedPreferences.contains(EMAIL) && sharedPreferences.contains(PASSWORD)) {
                val email = sharedPreferences.getString(EMAIL, "")
                val password = sharedPreferences.getString(PASSWORD, "")
                userViewModel.signInUser(email, password)
            } else {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }

    private fun observeLoginResponse() {
        userViewModel.user.observe(this, Observer {
            if (it != null) {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtra(EMAIL, it.email)
                finishAffinity()
                startActivity(intent)
            } else {
                showMessage(SOMETHING_WENT_WRONG)
                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .show()
    }
}