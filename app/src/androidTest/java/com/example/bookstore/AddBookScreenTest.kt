package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.dao.CommonBookDao
import com.example.bookstore.data.dao.IBookDao
import com.example.bookstore.data.di.MockBookDao
import com.example.bookstore.data.model.User
import com.example.bookstore.ui.screen.BookScreen.Screen.AddBookScreen
import com.example.bookstore.ui.theme.BookStoreTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class AddBookScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

//    @Inject
//    @MockBookDao
    lateinit var bookDao: IBookDao

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            BookStoreTheme {
                AddBookScreen(
                        navController = rememberNavController(),
                        bookDao = bookDao,
                        currentUser = User(id = 1, userName = "TestUser", password = "password", rePassword = "password", firstName = null, lastName = null, contactNumber = null, email = null, userStatus = null)
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

        composeTestRule.onNodeWithText("Book added successfully!").assertIsDisplayed()
    }

    @Test
    fun testButtonClick() {
        composeTestRule.setContent {
            AddBookScreen(
                    navController = rememberNavController(),
                    bookDao = bookDao,
                    currentUser = User(id = 1, userName = "TestUser", password = "password", rePassword = "password", firstName = null, lastName = null, contactNumber = null, email = null, userStatus = null)
            )
        }
        composeTestRule.onNodeWithText("Add Book").performClick()
        composeTestRule.onNodeWithText("Book added button pressed!").assertIsDisplayed()
    }
}
