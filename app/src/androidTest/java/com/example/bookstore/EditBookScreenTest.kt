package com.example.bookstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstore.data.ipackage.IBookDao
import com.example.bookstore.data.dao.MockBookDaoClass
import com.example.bookstore.ui.screen.BookScreen.Screen.EditBookScreen
import com.example.bookstore.ui.theme.BookStoreTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditBookScreenTest
{

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var bookDao: IBookDao

    @Before
    fun setUp()
    {
        bookDao = MockBookDaoClass()

        composeTestRule.setContent {
            val navController = rememberNavController()

            BookStoreTheme {
                EditBookScreen(
                        navController = navController,
                        bookDao = bookDao,
                        bookId = 1
                )
            }
        }
    }

    @Test
    fun testEditBookScreen_initialState()
    {
        composeTestRule.onNodeWithText("Edit Book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description of this book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Published Year of this book").assertIsDisplayed()

        composeTestRule.onNodeWithText("Mock Book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Author").assertIsDisplayed()
        composeTestRule.onNodeWithText("Testing purposesssssss").assertIsDisplayed()
        composeTestRule.onNodeWithText("2023").assertIsDisplayed()
    }

    @Test
    fun testEditBookScreen_saveChangesSuccess() {
        composeTestRule.onNodeWithText("Title").performTextInput("Updated Book Title")
        composeTestRule.onNodeWithText("Author").performTextInput("Updated Author")
        composeTestRule.onNodeWithText("Description of this book").performTextInput("Updated description.")
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("")

        composeTestRule.onNodeWithText("Save Changes").performClick()
//        composeTestRule.onNodeWithTag("CustomDialog").assertIsDisplayed()
        composeTestRule.onNodeWithText("Success", useUnmergedTree = true).assertIsDisplayed()

    }




    @Test
    fun testEditBookScreen_saveChangesFailInvalidYear()
    {
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("InvalidYear")

        composeTestRule.onNodeWithText("Save Changes").performClick()
        composeTestRule.onNodeWithText("Invalid published year").assertIsDisplayed()
    }

    @Test
    fun testEditBookScreen_saveChangesFailMissingTitle()
    {
        composeTestRule.onNodeWithText("Title").performTextClearance()

        composeTestRule.onNodeWithText("Save Changes").performClick()
        composeTestRule.onNodeWithText("Title is required").assertIsDisplayed()
    }

    @Test
    fun testEditBookScreen_saveChangesFailMissingAuthor()
    {
        composeTestRule.onNodeWithText("Author").performTextClearance()

        composeTestRule.onNodeWithText("Save Changes").performClick()
        composeTestRule.onNodeWithText("Author is required").assertIsDisplayed()
    }
}
