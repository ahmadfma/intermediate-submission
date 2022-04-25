package com.ahmadfma.intermediate_submission1.ui.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.helper.EspressoIdlingResource
import com.ahmadfma.intermediate_submission1.ui.detail.DetailActivity
import com.ahmadfma.intermediate_submission1.ui.main.MainActivity
import com.ahmadfma.intermediate_submission1.ui.maps.MapsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadStories() {
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }

    @Test
    fun when_click_story_should_navigate_to_DetailActivity() {
        Intents.init()
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Intents.intended(hasComponent(DetailActivity::class.java.name))
        onView(withId(R.id.storyImage)).check(matches(isDisplayed()))
        Intents.release()
    }

    @Test
    fun when_click_map_button_should_navigate_to_MapsActivity() {
        Intents.init()
        onView(withId(R.id.gotoMaps)).perform(click())
        Intents.intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        Intents.release()
    }

}