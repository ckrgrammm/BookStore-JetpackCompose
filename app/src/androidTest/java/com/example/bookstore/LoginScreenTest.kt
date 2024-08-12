package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookstore.ui.screen.UserScreen.Screen.LoginScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest
{

    @get:Rule
    val composeTestRule = createComposeRule()

//    @get:Rule
//    val mockitoRule = MockitoJUnit.rule()

    private lateinit var navController: NavHostController

    @Before
    fun setup()
    {
        navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)

    }

    @Test
    fun testLoginScreen_uiElementsDisplayed()
    {
        composeTestRule.setContent {
            LoginScreen(
                    navController = navController,
                    onLoginSuccess = {}
            )
        }

        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Not a member? Register here").assertIsDisplayed()
    }

    @Test
    fun testLoginScreen_loginSuccessful()
    {
        var loginSuccessCalled = false

        composeTestRule.setContent {
            LoginScreen(
                    navController = navController,
                    onLoginSuccess = {
                        loginSuccessCalled = true
                    }
            )
        }

        composeTestRule.onNodeWithText("Login").performClick()

        assert(loginSuccessCalled)
    }

    @Test
    fun testLoginScreen_loginFailureShowsErrorDialog()
    {
        composeTestRule.setContent {
            LoginScreen(
                    navController = navController,
                    onLoginSuccess = {}
            )
        }

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid Credentials").assertIsDisplayed()
    }
}
