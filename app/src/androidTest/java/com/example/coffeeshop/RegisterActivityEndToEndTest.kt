package com.example.coffeeshop


import androidx.test.core.app.ActivityScenario

import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Test
    fun testTypingInRegisterForm() {
        // navigate to register page
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click())
        // Fill in the registration form
        onView(withId(R.id.firstName)).perform(typeText("John"), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(typeText("Doe"), closeSoftKeyboard())
        onView(withId(R.id.email)).perform(typeText("johndoe@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("StrongPassword123!"), closeSoftKeyboard())
        onView(withId(R.id.dob)).perform(typeText("1990-01-01"), closeSoftKeyboard())
        scenario.close()
    }

    @Test
    fun registerUserEmptyFields() {

        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click())
        // Leave fields empty and click the register button
        onView(withId(R.id.registerButton2)).perform(click())

        // Verify that the appropriate error messages are displayed
        onView(withId(R.id.textViewMessage)).check(matches(withText("First name required!")))
        scenario.close()
    }
}