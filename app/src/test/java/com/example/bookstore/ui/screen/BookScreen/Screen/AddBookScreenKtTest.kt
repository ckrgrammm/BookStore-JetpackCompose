package com.example.bookstore.ui.screen.BookScreen.Screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.model.Book
import com.example.bookstore.data.model.User
import com.example.bookstore.ui.theme.BookStoreTheme
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import java.util.Date
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AddBookScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var bookDao: BookDao
    private lateinit var currentUser: User

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        bookDao = mock(BookDao::class.java)
        currentUser = User(id = 1, userName = "ray", password = "1234", email = null, contactNumber = null, rePassword = "1234", userProfile = null, firstName = null, lastName = null, userStatus = null)
    }

    @Test
    fun addBookScreen_addValidBook() {
        composeTestRule.setContent {
            BookStoreTheme {
                AddBookScreen(navController = navController, bookDao = bookDao, currentUser = currentUser)
            }
        }

        composeTestRule.onNodeWithText("Title").performTextInput("Test Book")
        composeTestRule.onNodeWithText("Author").performTextInput("Test Author")
        composeTestRule.onNodeWithText("Description of this book").performTextInput("This is a test description.")
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("2022")

        composeTestRule.onNodeWithText("Add Book").performClick()

        val expectedBook = Book(
                title = "Test Book",
                author = "Test Author",
                description = "This is a test description.",
                publishedYear = 2022,
                dateOfRegister = Date(),
                bookOwnerId = currentUser.id,
                imageUri = null
        )

        runBlocking {
            verify(bookDao, times(1)).insertBook(expectedBook)
        }

        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
        composeTestRule.onNodeWithText("Book added successfully!").assertIsDisplayed()

        composeTestRule.onNodeWithText("OK").performClick()
        composeTestRule.waitForIdle()

        assert(navController.currentBackStackEntry?.destination?.route == "home")
    }

    @Test
    fun addBookScreen_missingTitleShowsError() {
        composeTestRule.setContent {
            BookStoreTheme {
                AddBookScreen(navController = navController, bookDao = bookDao, currentUser = currentUser)
            }
        }

        composeTestRule.onNodeWithText("Author").performTextInput("Test Author")
        composeTestRule.onNodeWithText("Description of this book").performTextInput("This is a test description.")
        composeTestRule.onNodeWithText("Published Year of this book").performTextInput("2022")

        composeTestRule.onNodeWithText("Add Book").performClick()

        composeTestRule.onRoot().printToLog("TAG")

        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithText("Title is required").assertExists()
    }
}
