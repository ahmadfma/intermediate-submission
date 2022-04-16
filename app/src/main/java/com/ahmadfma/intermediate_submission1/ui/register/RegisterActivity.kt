package com.ahmadfma.intermediate_submission1.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.databinding.ActivityRegisterBinding
import com.ahmadfma.intermediate_submission1.helper.Validator
import com.ahmadfma.intermediate_submission1.ui.list_story.ListStoryActivity
import com.ahmadfma.intermediate_submission1.viewmodel.AuthenticationViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthenticationViewModel
    private var usernameInput = ""
    private var emailInput = ""
    private var passwordInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.registerProgressBar.visibility = View.GONE
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
    }

    private fun initListener() = with(binding)  {
        signUpBtn.setOnClickListener {
            if(Validator.isAllFormFilled(arrayOf(usernameInputRegister, emailInputRegister, passwordInputRegister))) {
                usernameInput = usernameInputRegister.text.toString()
                emailInput = emailInputRegister.text.toString()
                passwordInput = passwordInputRegister.text.toString()
                if(Validator.isFormValid(arrayOf(usernameInputRegister, emailInputRegister, passwordInputRegister))) {
                    registerUserListener()
                } else {
                    Toast.makeText(this@RegisterActivity, getString(R.string.error_form_not_valid), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@RegisterActivity, getString(R.string.error_form_not_filled), Toast.LENGTH_SHORT).show()
            }
        }

        signInBtnRegister.setOnClickListener {
            onBackPressed()
        }
    }

    private fun registerUserListener() {
        viewModel.registerUser(usernameInput, emailInput, passwordInput).observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d(TAG, "Result Loading")
                    showProgressBar(true)
                }
                is Result.Error -> {
                    showProgressBar(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showProgressBar(false)
                    val message = result.data
                    if(message != null) {
                        if(message.error) {
                            Log.d(TAG, "Result Success: error : ${message.message}")
                            Toast.makeText(this, message.message, Toast.LENGTH_SHORT).show()
                        } else {
                            loginUserListener()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loginUserListener() {
        viewModel.loginUser(emailInput, passwordInput).observe(this) { result ->
            when(result) {
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    showProgressBar(false)
                }
                is Result.Loading -> {
                    showProgressBar(true)
                }
                is Result.Success -> {
                    Log.d(TAG, "Result Success: ${result.data}")
                    val response = result.data
                    if(response != null) {
                        if(!response.error) {
                            Intent().apply {
                                setClass(this@RegisterActivity, ListStoryActivity::class.java)
                                startActivity(this)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            binding.registerProgressBar.visibility = View.VISIBLE
            binding.signUpBtn.visibility = View.GONE
        } else {
            binding.registerProgressBar.visibility = View.GONE
            binding.signUpBtn.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "RegisterActivity"
    }

}