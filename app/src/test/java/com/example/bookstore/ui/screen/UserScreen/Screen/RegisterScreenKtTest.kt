package com.example.bookstore.ui.screen.UserScreen.Screen

import org.junit.Test

import org.junit.Assert.*
import org.mockito.kotlin.whenever
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.bookstore.HiltTestApplication
import com.example.bookstore.ui.theme.BookStoreTheme
import com.example.bookstore.ui.screen.UserScreen.ViewModel.RegisterViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RegisterScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        registerViewModel = mock(RegisterViewModel::class.java)
    }

    @Test
    fun registerScreen_showsErrorDialog_whenPasswordsDoNotMatch() {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterScreen(navController = navController)
            }
        }

        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password321")
        composeTestRule.onNodeWithText("Register").performClick()

        // Debugging: Log the UI hierarchy
        composeTestRule.onRoot().printToLog("TAG")

        // Check if the error dialog is displayed
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithText("Passwords do not match.").assertExists()
    }

    @Test
    fun registerScreen_showsErrorDialog_whenRequiredFieldsAreEmpty() {
        composeTestRule.setContent {
            BookStoreTheme {
                RegisterScreen(navController = navController)
            }
        }

        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Register").performClick()

        // Debugging: Log the UI hierarchy
        composeTestRule.onRoot().printToLog("TAG")

        // Check if the error dialog is displayed
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithText("Please fill up all the required fields.").assertExists()
    }

    @Test
    fun registerScreen_showsErrorDialog_whenUsernameIsTaken() {
        runBlockingTest {
            whenever(registerViewModel.isUsernameTaken("testuser")).thenReturn(true)

            composeTestRule.setContent {
                BookStoreTheme {
                    RegisterScreen(navController = navController)
                }
            }

            composeTestRule.onNodeWithText("Username").performTextInput("testuser")
            composeTestRule.onNodeWithText("Password").performTextInput("password123")
            composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password123")
            composeTestRule.onNodeWithText("Register").performClick()

            // Debugging: Log the UI hierarchy
            composeTestRule.onRoot().printToLog("TAG")

            // Check if the error dialog is displayed
            composeTestRule.onNodeWithText("Error").assertExists()
            composeTestRule.onNodeWithText("Username is already taken.").assertExists()
        }
    }

    @Test
    fun registerScreen_showsSuccessDialog_whenRegistrationIsSuccessful() {
        runBlockingTest {
            whenever(registerViewModel.isUsernameTaken("testuser")).thenReturn(false)

            composeTestRule.setContent {
                BookStoreTheme {
                    RegisterScreen(navController = navController)
                }
            }

            composeTestRule.onNodeWithText("Username").performTextInput("testuser")
            composeTestRule.onNodeWithText("Password").performTextInput("password123")
            composeTestRule.onNodeWithText("Re-enter Password").performTextInput("password123")
            composeTestRule.onNodeWithText("Register").performClick()

            // Debugging: Log the UI hierarchy
            composeTestRule.onRoot().printToLog("TAG")

            // Check if the success dialog is displayed
            composeTestRule.onNodeWithText("Success").assertExists()
            composeTestRule.onNodeWithText("Register Successfully!").assertExists()
        }
    }

    private fun runBlockingTest(block: suspend () -> Unit) {
        runBlocking {
            block()
        }
    }
}

//annotation class
