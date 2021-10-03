package com.app.cementfactory

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.app.cementfactory.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)

class LoginTest {

    @get:Rule
    val uiTestRule = ActivityScenarioRule(LoginActivity::class.java);

    @Test
    fun testLogin() {
        onView(withId(R.id.etFullname))
            .perform(clearText())
            .perform(typeText("dipak12345"));
        closeSoftKeyboard()
        Thread.sleep(1500);

        onView(withId(R.id.etPassword))
            .perform(clearText())
            .perform(typeText("dipak12345"));
        Thread.sleep(1500);
        closeSoftKeyboard();

        onView(withId(R.id.btnLogin))
            .perform(click());

        Thread.sleep(2000);


    }
}