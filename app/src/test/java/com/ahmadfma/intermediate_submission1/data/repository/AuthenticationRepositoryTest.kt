package com.ahmadfma.intermediate_submission1.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ahmadfma.intermediate_submission1.data.FakeApiService
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.utils.MainCoroutineRule
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthenticationRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = FakeApiService()
    }

    @Test
    fun `when username & email & password valid, register should success`() = runTest {
        val expectedValue = Response.success(MessageResponse(false, FakeApiService.SUCCESS))
        val username = "ahmad"
        val email = "ahmadfathanah@gmail.com"
        val password = "123456"
        val actualValue = apiService.registerUser(username, email, password)
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(!actualValue.body()!!.error)
        Assert.assertTrue(actualValue.body() == expectedValue.body())
    }

    @Test
    fun `when email and password valid, login should success`() = runTest {
        val expectedValue = Response.success(LoginResponse(false, FakeApiService.SUCCESS))
        val email = "ahmadfathanah05@gmail.com"
        val password = "123456"
        val actualValue = apiService.loginUser(email, password)
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(!actualValue.body()!!.error)
        Assert.assertTrue(actualValue.body() == expectedValue.body())
    }

}
