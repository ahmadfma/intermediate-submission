package com.ahmadfma.intermediate_submission1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.databinding.ActivityLoginBinding
import com.ahmadfma.intermediate_submission1.helper.Validator
import com.ahmadfma.intermediate_submission1.ui.list_story.ListStoryActivity
import com.ahmadfma.intermediate_submission1.ui.register.RegisterActivity
import com.ahmadfma.intermediate_submission1.viewmodel.AuthenticationViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var emailInput = ""
    private var passwordInput = ""
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loginProgressBar.visibility = View.GONE
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
    }

    private fun initListener() = with(binding) {
        signInBtn.setOnClickListener {
            if(Validator.isAllFormFilled(arrayOf(emailInputLogin, passwordInputLogin))) {
                emailInput = emailInputLogin.text.toString()
                passwordInput = passwordInputLogin.text.toString()
                if(Validator.isFormValid(arrayOf(emailInputLogin, passwordInputLogin))) {
                    loginUserListener()
                } else {
                    Toast.makeText(this@LoginActivity, getString(R.string.error_form_not_valid), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, getString(R.string.error_form_not_filled), Toast.LENGTH_SHORT).show()
            }
        }

        signUpBtnLogin.setOnClickListener {
            Intent().apply {
                setClass(this@LoginActivity, RegisterActivity::class.java)
                startActivity(this)
            }
        }
    }

    private fun loginUserListener() {
        viewModel.loginUser(emailInput, passwordInput).observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    showProgressBar(true)
                }
                is Result.Error -> {
                    showProgressBar(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showProgressBar(false)
                    val response = result.data
                    Log.d(TAG, "Result Success: response : $response")
                    if (response != null) {
                        if (!response.error) {
                            Intent().apply {
                                setClass(this@LoginActivity, ListStoryActivity::class.java)
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
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.signInBtn.visibility = View.GONE
        } else {
            binding.loginProgressBar.visibility = View.GONE
            binding.signInBtn.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "LoginActivity"
    }

}