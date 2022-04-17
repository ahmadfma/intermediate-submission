package com.ahmadfma.intermediate_submission1.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.databinding.ActivityMainBinding
import com.ahmadfma.intermediate_submission1.ui.add_story.AddStoryActivity
import com.ahmadfma.intermediate_submission1.ui.main.fragment.home.HomeFragment
import com.ahmadfma.intermediate_submission1.ui.main.fragment.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        setCurrentFragment(HomeFragment())
    }

    private fun initListener() = with(binding) {
        bottomAppbarMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> setCurrentFragment(HomeFragment())
                R.id.nav_profile -> setCurrentFragment(ProfileFragment())
            }
            true
        }

        newStoryBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

    private fun setCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragmentContainer, fragment)
        commit()
    }

    companion object {
        const val TAG = "MainActivity"
    }

}