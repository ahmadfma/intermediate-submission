package com.ahmadfma.intermediate_submission1.helper

import org.junit.Assert
import org.junit.Test

class ValidatorTest {

    @Test
    fun `when email is valid should return true`() {
        val email = "ahmadfathanah05@gmail.com"
        val expectedValue = true
        val actualValue = Validator.isEmailValid(email)
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `when email is invalid should return false`() {
        val expectedValue = false
        val email1 = "ahmadfathanah05gmail.com"
        val email2 = "@gmail.com"
        val email3 = "ahmadfathanah05@gmail"
        val email4 = "ahmadfathanah05@.com"
        val actualValue1 = Validator.isEmailValid(email1)
        val actualValue2 = Validator.isEmailValid(email2)
        val actualValue3 = Validator.isEmailValid(email3)
        val actualValue4 = Validator.isEmailValid(email4)
        Assert.assertEquals(expectedValue, actualValue1)
        Assert.assertEquals(expectedValue, actualValue2)
        Assert.assertEquals(expectedValue, actualValue3)
        Assert.assertEquals(expectedValue, actualValue4)
    }

}