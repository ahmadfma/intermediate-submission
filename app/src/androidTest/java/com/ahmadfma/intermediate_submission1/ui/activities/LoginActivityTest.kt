package com.ahmadfma.intermediate_submission1.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.helper.EspressoIdlingResource
import com.ahmadfma.intermediate_submission1.ui.detail.DetailActivity
import com.ahmadfma.intermediate_submission1.ui.login.LoginActivity
import com.ahmadfma.intermediate_submission1.ui.register.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login() {
        print("start login")
        onView(withId(R.id.emailInputLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.emailInputLogin)).perform(click())
        onView(withId(R.id.emailInputLogin)).perform(typeText("ahmadfathanah05@gmail.com"))
        onView(withId(R.id.passwordInputLogin)).perform(click())
        onView(withId(R.id.passwordInputLogin)).perform(typeText("123456"))
        onView(withId(R.id.signInBtn)).perform(click())
        print("end login")
    }

    @Test
    fun gotoRegisterActivity() {
        print("start gotoRegisterActivity")
        Intents.init()
        onView(withId(R.id.signUpBtnLogin)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
        Intents.release()
        print("end gotoRegisterActivity")
    }

}