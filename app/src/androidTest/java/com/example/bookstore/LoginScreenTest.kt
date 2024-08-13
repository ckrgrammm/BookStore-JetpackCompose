package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookstore.ui.screen.UserScreen.Screen.LoginContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest
{

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavHostController

    @Before
    fun setup()
    {
        navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testLoginContent_uiElementsDisplayed()
    {
        composeTestRule.setContent {
            LoginContent(
                    userName = "",
                    onUserNameChange = {},
                    password = "",
                    onPasswordChange = {},
                    onLoginClick = {},
                    onRegisterClick = {},
                    showDialog = false,
                    dialogTitle = "",
                    dialogMessage = "",
                    onDismissDialog = {}
            )
        }

        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Not a member? Register here").assertIsDisplayed()
    }

    @Test
    fun testLoginContent_loginSuccessful()
    {
        var loginSuccessCalled = false

        composeTestRule.setContent {
            LoginContent(
                    userName = "",
                    onUserNameChange = {},
                    password = "",
                    onPasswordChange = {},
                    onLoginClick = { loginSuccessCalled = true },
                    onRegisterClick = {},
                    showDialog = false,
                    dialogTitle = "",
                    dialogMessage = "",
                    onDismissDialog = {}
            )
        }

        composeTestRule.onNodeWithText("Login").performClick()

        assert(loginSuccessCalled)
    }

    @Test
    fun testLoginContent_loginFailureShowsErrorDialog()
    {
        composeTestRule.setContent {
            LoginContent(
                    userName = "",
                    onUserNameChange = {},
                    password = "",
                    onPasswordChange = {},
                    onLoginClick = {},
                    onRegisterClick = {},
                    showDialog = true,
                    dialogTitle = "Error",
                    dialogMessage = "Invalid Credentials",
                    onDismissDialog = {}
            )
        }

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid Credentials").assertIsDisplayed()
    }
}
