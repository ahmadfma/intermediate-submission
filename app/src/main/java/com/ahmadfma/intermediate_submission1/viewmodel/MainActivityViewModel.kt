package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.ahmadfma.intermediate_submission1.ui.main.fragment.home.HomeFragment

class MainActivityViewModel: ViewModel() {

    var selectedFragment : Fragment = HomeFragment()

}