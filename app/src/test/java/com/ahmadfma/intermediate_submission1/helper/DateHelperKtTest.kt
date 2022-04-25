package com.ahmadfma.intermediate_submission1.helper


import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.text.ParseException


class DateHelperKtTest {

    @Test
    fun `when story's date format is valid, parsing should success`() {
        val expectedOutput = "Monday, April 25, 2022 14:07 PM"
        val createdAt = "2022-04-25T14:07:11.601Z"
        Assert.assertEquals(expectedOutput, createdAt.convertToDate())
    }

    @Test
    fun `when story's date format is valid, parsing should failed`() {
        Assert.assertThrows(ParseException::class.java) {
            "2022-04-25 14:07:11.601Z".convertToDate()
        }
    }

}