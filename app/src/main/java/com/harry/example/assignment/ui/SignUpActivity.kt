package com.harry.example.assignment.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.harry.example.assignment.R
import com.harry.example.assignment.database.entities.User
import com.harry.example.assignment.utility.*
import com.harry.example.assignment.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class SignUpActivity : AppCompatActivity(), View.OnFocusChangeListener {
    companion object {
        val TAG = "TAG"
    }

    private val userViewModel: UserViewModel by viewModel()
    private val utility: Utility by inject()
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var confirmpassword: String? = null
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        observeValidationResposne()
        observeSignUpResponse()
        observeLoginProgress()
        user_name.setOnFocusChangeListener(this)
        user_mail.setOnFocusChangeListener(this)
        user_password.setOnFocusChangeListener(this)
        confirm_password.setOnFocusChangeListener(this)
        sign_up.setOnClickListener {
            name = user_name.text.toString()
            email = user_mail.text.toString()
            password = user_password.text.toString()
            confirmpassword = confirm_password.text.toString()
            utility.validateCredentials(name, email, password, confirmpassword)
            enableOrDisable(false)
            showProgress()
        }
    }

    private fun observeValidationResposne() {
        utility.validationResponse.observe(this, Observer {
            when (it) {
                NAME_CANNOT_BE_EMPTY -> {
                    showMessage(NAME_CANNOT_BE_EMPTY)
                    sign_up_input_layout_name.error = NAME_CANNOT_BE_EMPTY
                    enableOrDisable(true)
                    hideProgress()
                }
                NAME_SHOULD_CONTAIN_ALPHABETS_ONLY -> {
                    showMessage(NAME_SHOULD_CONTAIN_ALPHABETS_ONLY)
                    sign_up_input_layout_name.error = NAME_SHOULD_CONTAIN_ALPHABETS_ONLY
                    enableOrDisable(true)
                    hideProgress()
                }
                EMAIL_CANNOT_BE_EMPTY -> {
                    showMessage(EMAIL_CANNOT_BE_EMPTY)
                    sign_up_input_layout_mail.error = EMAIL_CANNOT_BE_EMPTY
                    enableOrDisable(true)
                    hideProgress()
                }
                INVALID_MAIL_ID -> {
                    showMessage(INVALID_MAIL_ID)
                    sign_up_input_layout_mail.error = INVALID_MAIL_ID
                    enableOrDisable(true)
                    hideProgress()
                }
                PASSWORD_CANNOT_BE_EMPTY -> {
                    showMessage(PASSWORD_CANNOT_BE_EMPTY)
                    sign_up_input_layout_password.error = PASSWORD_CANNOT_BE_EMPTY
                    enableOrDisable(true)
                    hideProgress()
                }
                PASSWORD_SHOULD_BE_SAME -> {
                    showMessage(PASSWORD_SHOULD_BE_SAME)
                    sign_up_input_layout_confirm_password.error = PASSWORD_SHOULD_BE_SAME
                    enableOrDisable(true)
                    hideProgress()
                }
                VALID_CREDENTIALS -> {
                    user = User(name, email, password)
                    userViewModel.signUpUser(user)
                    enableOrDisable(true)
                    hideProgress()
                }
            }
        })
    }

    private fun showMessage(response: String) {
        Snackbar.make(findViewById<View>(android.R.id.content), response, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun enableOrDisable(res: Boolean) {
        group.referencedIds.forEach {
            findViewById<View>(it).isEnabled = res
        }
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
    }

    private fun observeSignUpResponse() {
        userViewModel.signUpResponse.observe(this, Observer {
            when (it) {
                USER_REGISTERED_SUCCESSFULLY -> {
                    showMessage(USER_REGISTERED_SUCCESSFULLY)
                    saveCredentials()
                    toHomeActivity()
                    clearView()
                }
                EMAIL_ALREADY_EXISTS -> {
                    showMessage(EMAIL_ALREADY_EXISTS)
                    sign_up_input_layout_mail.error = EMAIL_ALREADY_EXISTS
                }
                else -> showMessage(USER_REGISTERATION_FAILED)
            }
        })
    }

    private fun observeLoginProgress() {
        userViewModel.loggingIn.observe(this, Observer {
            if (it == false)
                hideProgress()
            enableOrDisable(true)
        })
    }

    private fun clearView() {
        user_mail.text = getEmptyEditable()
        user_name.text = getEmptyEditable()
        user_password.text = getEmptyEditable()
        confirm_password.text = getEmptyEditable()
    }

    private fun getEmptyEditable(): Editable {
        return Editable.Factory.getInstance().newEditable("")
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        val view_id = v?.id
        when (view_id) {
            R.id.user_name -> {
                if (sign_up_input_layout_name.isErrorEnabled)
                    sign_up_input_layout_name.error = ""
            }
            R.id.user_mail -> {
                if (sign_up_input_layout_mail.isErrorEnabled)
                    sign_up_input_layout_mail.error = ""
            }
            R.id.user_password -> {
                if (sign_up_input_layout_password.isErrorEnabled)
                    sign_up_input_layout_password.error = ""
            }
            R.id.confirm_password -> {
                if (sign_up_input_layout_confirm_password.isErrorEnabled)
                    sign_up_input_layout_confirm_password.error = ""
            }
        }
    }

    private fun toHomeActivity() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra(EMAIL, user?.email)
        intent.flags=Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivity(intent)
        finishAffinity()
    }

    private fun saveCredentials() {
        val sharedPreferences = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, user?.email)
        editor.putString(PASSWORD, user?.password)
        editor.apply()
    }
}