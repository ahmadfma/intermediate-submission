package com.ahmadfma.intermediate_submission1.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.local.UserPreferences
import com.ahmadfma.intermediate_submission1.ui.login.LoginActivity
import com.ahmadfma.intermediate_submission1.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        lifecycleScope.launch {
            delay(1500)
            val intent = Intent()
            val pref = UserPreferences(this@SplashActivity)
            val token = pref.getUser().token
            if(token != null && token.isNotEmpty()) {
                intent.setClass(this@SplashActivity, MainActivity::class.java)
            } else {
                intent.setClass(this@SplashActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}