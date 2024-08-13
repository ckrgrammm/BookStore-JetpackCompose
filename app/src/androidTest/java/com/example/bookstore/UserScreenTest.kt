package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.data.mock.MockUserDaoClass
import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.data.model.User
import com.example.bookstore.ui.screen.UserScreen.Screen.UserScreen
import com.example.bookstore.ui.theme.BookStoreTheme
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserScreenTest
{

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var userDao: IUserDao
    private lateinit var currentUser: User

    @Before
    fun setUp()
    {
        userDao = MockUserDaoClass()
        currentUser = User(
                id = 1,
                userName = "testuser",
                password = "password",
                rePassword = "password",
                firstName = "Test",
                lastName = "User",
                email = "testuser@example.com",
                contactNumber = "1234567890",
                userProfile = null,
                userStatus = null
        )

        composeTestRule.setContent {
            val navController = rememberNavController()

            BookStoreTheme {
                UserScreen(
                        navController = navController,
                        userDao = userDao,
                        currentUser = currentUser
                )
            }
        }
    }

    @Test
    fun testUserScreen_initialState()
    {
        composeTestRule.onNodeWithText("Edit Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("First Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Last Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contact Number").assertIsDisplayed()
    }

    @Test
    fun testUserScreen_updateProfileSuccess()
    {
        runBlocking {
            composeTestRule.onNodeWithText("First Name").performTextInput("Updated")
            composeTestRule.onNodeWithText("Last Name").performTextInput("Updated")
            composeTestRule.onNodeWithText("Email").performTextInput("")
            composeTestRule.onNodeWithText("Contact Number").performTextInput("")

            composeTestRule.onNodeWithText("Update").performClick()

            composeTestRule.onNodeWithText("Success").assertIsDisplayed()
        }
    }

    @Test
    fun testUserScreen_updateProfileFailure()
    {
        composeTestRule.onNodeWithText("First Name").performTextClearance()

        composeTestRule.onNodeWithText("Update").performClick()
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

//    @Test
//    fun testUserScreen_logout() {
//        composeTestRule.onNodeWithText("Logout").performClick()
//    }


    @Test
    fun testUserScreen_profileImageClick()
    {
        composeTestRule.onNodeWithContentDescription("Profile Picture").performClick()
    }
}
