package com.ahmadfma.intermediate_submission1.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.databinding.ActivityRegisterBinding
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
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
    }

    private fun initListener() = with(binding)  {
        signUpBtn.setOnClickListener {
            if(isAllFormFilled()) {
                if(isFormValid()) {
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
            if(result != null) {
                when(result) {
                    is Result.Loading -> {
                        Log.d(TAG, "Result loading")
                    }
                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        val message = result.data
                        if(message != null) {
                            if(message.error) {
                                Log.d(TAG, "Result Success: error : ${message.message}")
                                Toast.makeText(this, message.message, Toast.LENGTH_SHORT).show()
                            } else {
                                loginUserListener()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loginUserListener() {

    }

    private fun isAllFormFilled(): Boolean = with(binding) {
        usernameInput = usernameInputRegister.text.toString()
        emailInput = emailInputRegister.text.toString()
        passwordInput = passwordInputRegister.text.toString()
        return usernameInput.isNotEmpty() &&
                emailInput.isNotEmpty() &&
                passwordInput.isNotEmpty()
    }

    private fun isFormValid(): Boolean = with(binding) {
        return usernameInputRegister.error == null &&
                emailInputRegister.error == null &&
                passwordInputRegister.error == null
    }

    companion object {
        const val TAG = "RegisterActivity"
    }

}