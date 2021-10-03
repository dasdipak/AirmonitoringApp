package com.app.cementfactory

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.app.cementfactory.ui.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)

class SignupTest {

    @get:Rule
    val uiTestRule = ActivityScenarioRule(RegisterActivity::class.java);

    @Test
    fun testSignUp() {
        Espresso.onView(ViewMatchers.withId(R.id.etFullname))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("Dipak Das"));
        Espresso.closeSoftKeyboard()
        Thread.sleep(1500);

        Espresso.onView(ViewMatchers.withId(R.id.etPhone))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("9804781699"));
        Thread.sleep(1500);
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("dipakdas123"));
        Thread.sleep(1500);
        Espresso.closeSoftKeyboard();



        Espresso.onView(ViewMatchers.withId(R.id.etEmail))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("dasdipak123@gmail.com"));
        Thread.sleep(1500);
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("dipak123"));
        Thread.sleep(1500);
        Espresso.closeSoftKeyboard();


        Espresso.onView(ViewMatchers.withId(R.id.btnRegister))
            .perform(ViewActions.click());

        Thread.sleep(2000);


    }
}