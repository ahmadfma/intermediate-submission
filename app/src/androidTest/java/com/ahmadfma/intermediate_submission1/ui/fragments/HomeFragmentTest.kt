package com.ahmadfma.intermediate_submission1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.remote.RetrofitClient
import com.ahmadfma.intermediate_submission1.helper.EspressoIdlingResource
import com.ahmadfma.intermediate_submission1.ui.main.fragment.home.HomeFragment
import com.ahmadfma.intermediate_submission1.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        RetrofitClient.Base_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun fetch_story_success() {
        val bundle = Bundle()
        launchFragmentInContainer<HomeFragment>(bundle, R.style.Theme_Intermediate_Submission1)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("story_response_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withText("Lorem Ipsum")).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Dimas3"))
                )
            )
    }

}
