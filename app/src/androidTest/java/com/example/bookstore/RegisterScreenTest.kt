package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.data.mock.MockUserDaoClass
import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.ui.screen.UserScreen.Screen.RegisterScreen
import com.example.bookstore.ui.theme.BookStoreTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var userDao: IUserDao

    @Before
    fun setUp() {
        userDao = MockUserDaoClass()

        composeTestRule.setContent {
            val navController = rememberNavController()

            BookStoreTheme {
                RegisterScreen(
                        navController = navController,
                )
            }
        }
    }

    @Test
    fun testRegisterScreen_initialState() {
        composeTestRule.onNodeWithText("Book Store Register").assertIsDisplayed()
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Re-enter Password").assertIsDisplayed()
    }

    @Test
    fun testRegisterScreen_passwordsDoNotMatch() {
        composeTestRule.onNodeWithText("Username").performTextInput("NewUser")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Re-enter Password").performTextInput("differentpassword")

        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.onNodeWithText("Passwords do not match.").assertIsDisplayed()
    }

    @Test
    fun testRegisterScreen_usernameAlreadyTaken() {
        composeTestRule.onNodeWithText("Username").performTextInput("Testing")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password")

        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.onNodeWithText("Username is already taken.").assertIsDisplayed()
    }

    @Test
    fun testRegisterScreen_successfulRegistration() {
        composeTestRule.onNodeWithText("Username").performTextInput("NewUser")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password")

        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }
}
