package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.ui.screen.UserScreen.Screen.RegisterContent
import com.example.bookstore.ui.theme.BookStoreTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest
{

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun testRegisterContent_initialState()
    {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterContent(
                        userName = "",
                        onUserNameChange = {},
                        password = "",
                        onPasswordChange = {},
                        rePassword = "",
                        onRePasswordChange = {},
                        onRegisterClick = {},
                        errorMessage = null,
                        showDialog = false,
                        dialogTitle = "",
                        dialogMessage = "",
                        onDismissDialog = {},
                        onConfirmDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Book Store Register").assertIsDisplayed()
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Re-enter Password").assertIsDisplayed()
    }

    @Test
    fun testRegisterContent_passwordsDoNotMatch()
    {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterContent(
                        userName = "NewUser",
                        onUserNameChange = {},
                        password = "password",
                        onPasswordChange = {},
                        rePassword = "differentpassword",
                        onRePasswordChange = {},
                        onRegisterClick = {},
                        errorMessage = null,
                        showDialog = true,
                        dialogTitle = "Error",
                        dialogMessage = "Passwords do not match.",
                        onDismissDialog = {},
                        onConfirmDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Passwords do not match.").assertIsDisplayed()
    }

    @Test
    fun testRegisterContent_usernameAlreadyTaken()
    {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterContent(
                        userName = "Testing",
                        onUserNameChange = {},
                        password = "password",
                        onPasswordChange = {},
                        rePassword = "password",
                        onRePasswordChange = {},
                        onRegisterClick = {},
                        errorMessage = null,
                        showDialog = true,
                        dialogTitle = "Error",
                        dialogMessage = "Username is already taken.",
                        onDismissDialog = {},
                        onConfirmDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Username is already taken.").assertIsDisplayed()
    }

    @Test
    fun testRegisterContent_successfulRegistration()
    {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterContent(
                        userName = "NewUser",
                        onUserNameChange = {},
                        password = "password",
                        onPasswordChange = {},
                        rePassword = "password",
                        onRePasswordChange = {},
                        onRegisterClick = {},
                        errorMessage = null,
                        showDialog = true,
                        dialogTitle = "Success",
                        dialogMessage = "Registered Successfully!",
                        onDismissDialog = {},
                        onConfirmDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }
}
