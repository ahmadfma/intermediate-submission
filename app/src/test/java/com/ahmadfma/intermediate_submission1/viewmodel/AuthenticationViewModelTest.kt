package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.repository.AuthenticationRepository
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.utils.MainCoroutineRule
import com.ahmadfma.intermediate_submission1.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.ahmadfma.intermediate_submission1.helper.Validator

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthenticationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var authenticationViewModel: AuthenticationViewModel

    @Before
    fun setup() {
        authenticationViewModel = AuthenticationViewModel(authenticationRepository)
    }

    @Test
    fun `when username & email & password valid, register should be success`()  {
        val expectedMessage = "Register Success"
        val expectedResponse = MessageResponse(error = false, message = expectedMessage)
        val expectedValue = MutableLiveData<Result<MessageResponse?>>()
        expectedValue.value = Result.Success(expectedResponse)

        val username = "ahmad"
        val email = "ahmadfathanah05@gmail.com"
        val password = "123456"

        `when`(authenticationViewModel.registerUser(username, email, password)).thenReturn(expectedValue)
        val actualValue = authenticationViewModel.registerUser(username, email, password).getOrAwaitValue()
        Mockito.verify(authenticationRepository).register(username, email, password)

        Assert.assertTrue(username.isNotEmpty())
        Assert.assertTrue(Validator.isEmailValid(email))
        Assert.assertTrue(password.length >= 6)
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(actualValue is Result.Success)
        Assert.assertEquals(expectedMessage, (actualValue as Result.Success).data?.message)
    }

    @Test
    fun `when email already taken, register should failed`() {
        val expectedMessage = "Email already taken"
        val expectedValue = MutableLiveData<Result<MessageResponse?>>()
        expectedValue.value = Result.Error(expectedMessage)

        val username = "ahmad"
        val email = "ahmadfathanah05@gmail.com"
        val password = "123456"
        `when`(authenticationViewModel.registerUser(username, email, password)).thenReturn(expectedValue)
        val actualValue = authenticationViewModel.registerUser(username, email, password).getOrAwaitValue()
        Mockito.verify(authenticationRepository).register(username, email, password)

        Assert.assertTrue(actualValue is Result.Error)
        Assert.assertTrue((actualValue as Result.Error).error == expectedMessage)
    }

    @Test
    fun `when email and password valid, login should be success`() {
        val expectedMessage = "Login Success"
        val expectedResponse = LoginResponse(error = false, message = expectedMessage)
        val expectedValue = MutableLiveData<Result<LoginResponse?>>()
        expectedValue.value = Result.Success(expectedResponse)

        val email = "ahmadfathanah05@gmail.com"
        val password = "123456"

        `when`(authenticationViewModel.loginUser(email, password)).thenReturn(expectedValue)
        val actualValue = authenticationViewModel.loginUser(email, password).getOrAwaitValue()
        Mockito.verify(authenticationRepository).login(email, password)

        Assert.assertTrue(Validator.isEmailValid(email))
        Assert.assertTrue(password.length >= 6)
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(actualValue is Result.Success)
        Assert.assertEquals(expectedMessage, (actualValue as Result.Success).data?.message)
    }
}