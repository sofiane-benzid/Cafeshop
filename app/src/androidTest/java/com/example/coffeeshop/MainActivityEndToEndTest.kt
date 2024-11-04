package com.example.coffeeshop

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEndToEndTest {

    @Test
    fun testMainActivityTextElements() {
        // Launch the MainActivity using ActivityScenario
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Check if the title is displayed correctly
        onView(withId(R.id.titleId))
            .check(matches(isDisplayed()))
            .check(matches(withText("Café Mobile Application")))

        // Check if the description is displayed correctly
        onView(withId(R.id.description))
            .check(matches(isDisplayed()))
            .check(matches(withText("It is a good day to get coffee, get started.")))

        // Close the scenario
        scenario.close()
    }
    @Test
    fun testMainActivityButtons() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // check register button text and then click
        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click())
        // return to main page
        pressBack()
        // check login button text and then click
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed())).perform(click())
        scenario.close()
    }

}
