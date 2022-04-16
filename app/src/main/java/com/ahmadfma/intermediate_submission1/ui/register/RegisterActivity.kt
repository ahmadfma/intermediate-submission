package com.ahmadfma.intermediate_submission1.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signInBtnRegister.setOnClickListener {
                onBackPressed()
            }
        }
    }
}