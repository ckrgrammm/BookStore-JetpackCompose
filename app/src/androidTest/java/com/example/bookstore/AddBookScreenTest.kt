package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.data.ipackage.IBookDao
import com.example.bookstore.data.mock.MockBookDaoClass
import com.example.bookstore.data.model.User
import com.example.bookstore.ui.screen.BookScreen.Screen.AddBookScreen
import com.example.bookstore.ui.theme.BookStoreTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddBookScreenTest {

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var bookDao: IBookDao

    @Before
    fun setUp() {
        bookDao = MockBookDaoClass()

        composeTestRule.setContent {
            val navController = rememberNavController()

            BookStoreTheme {
                AddBookScreen(
                        navController = navController,
                        bookDao = bookDao,
                        currentUser = User(
                                id = 1,
                                userName = "TestUser",
                                password = "password",
                                rePassword = "password",
                                firstName = null,
                                lastName = null,
                                contactNumber = null,
                                email = null,
                                userStatus = null
                        )
                )
            }
        }
    }

    @Test
    fun testAddBookScreen_initialState() {
        composeTestRule.onNodeWithText("Add New Book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description of this book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Published Year of this book").assertIsDisplayed()
    }

    @Test
    fun testAddBookScreen_addBookSuccess() {
        composeTestRule.onNodeWithText("Title").performTextInput("Test Book Title")
        composeTestRule.onNodeWithText("Author").performTextInput("Test Author")
        composeTestRule.onNodeWithText("Description of this book").performTextInput("This is a test description.")
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("2023")

        composeTestRule.onNodeWithText("Add Book").performClick()
        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }

    @Test
    fun testAddBookScreen_addBookFail() {
        composeTestRule.onNodeWithText("Author").performTextInput("Test Author")
        composeTestRule.onNodeWithText("Description of this book").performTextInput("This is a test description.")
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("2023")

        composeTestRule.onNodeWithText("Add Book").performClick()
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }
}
