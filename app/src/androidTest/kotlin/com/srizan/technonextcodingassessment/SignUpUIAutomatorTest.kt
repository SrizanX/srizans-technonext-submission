package com.srizan.technonextcodingassessment

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpUIAutomatorTest {


    @Test
    fun testSignUp() = uiAutomator {
        startApp("com.srizan.technonextcodingassessment")
        onElement { textAsString() == "Sign Up" }.click()
        onElement {
            contentDescription == "Email input field"
        }.setText("testuser@example.com")
        onElement {
            contentDescription == "Password input field"
        }.setText("Test#Password123")
        onElement { contentDescription == "Confirm password input field" }.setText("TestPassword123")
        onElement { textAsString() == "Sign Up" }.click()
    }

}