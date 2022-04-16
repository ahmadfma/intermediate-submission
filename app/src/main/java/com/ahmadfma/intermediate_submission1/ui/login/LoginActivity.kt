package com.ahmadfma.intermediate_submission1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.databinding.ActivityLoginBinding
import com.ahmadfma.intermediate_submission1.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signUpBtnLogin.setOnClickListener {
                Intent().apply {
                    setClass(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(this)
                }
            }
        }

    }


}