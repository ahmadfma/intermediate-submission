package com.ahmadfma.intermediate_submission1.viewmodel

import com.ahmadfma.intermediate_submission1.ui.main.fragment.home.HomeFragment
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @Mock
    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Test
    fun `getSelectedFragment should return selected fragment`() {
        val expectedValue = HomeFragment()
        `when`(mainActivityViewModel.selectedFragment).thenReturn(expectedValue)
        val actualValue = mainActivityViewModel.selectedFragment
        Mockito.verify(mainActivityViewModel).selectedFragment
        Assert.assertEquals(expectedValue, actualValue)
    }

}