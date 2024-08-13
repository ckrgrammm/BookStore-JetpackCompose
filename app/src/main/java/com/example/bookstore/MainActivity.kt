package com.example.bookstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.common.UserSession
import com.example.bookstore.common.nav.BottomNavigationBar
import com.example.bookstore.ui.screen.BookScreen.Screen.AddBookScreen
import com.example.bookstore.ui.screen.BookScreen.Screen.BookDetailScreen
import com.example.bookstore.ui.screen.BookScreen.Screen.EditBookScreen
import com.example.bookstore.ui.screen.HomeScreen.HomeScreen
import com.example.bookstore.ui.screen.UserScreen.Screen.LoginScreen
import com.example.bookstore.ui.screen.UserScreen.Screen.RegisterScreen
import com.example.bookstore.ui.screen.UserScreen.Screen.UserScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.dao.UserDao

@AndroidEntryPoint
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
class MainActivity : ComponentActivity()
{

    @Inject
    lateinit var bookDao: BookDao

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentUser = UserSession.getCurrentUser()
            Log.d("MainActivity", "Current User $currentUser")

            Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute !in listOf("login", "register"))
                        {
                            BottomNavigationBar(navController = navController)
                        }
                    }
            ) { padding ->
                Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                ) {
                    NavHost(navController = navController, startDestination = if (currentUser == null) "login" else "home") {
                        composable("login") {
                            LoginScreen(navController) { user ->
                                UserSession.login(user)
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                        composable("register") {
                            RegisterScreen(navController)
                        }
                        composable("bookDetail/{bookId}") { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull()
                            bookId?.let { BookDetailScreen(navController, bookDao, it) }
                        }
                        composable("editBook/{bookId}") { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull()
                            bookId?.let { EditBookScreen(navController, bookDao, it) }
                        }

                        composable("home") {
                            val currentUser = UserSession.getCurrentUser()
                            Log.d("MainActivity", "Navigating to HomeScreen with user: $currentUser")
                            currentUser?.let {
                                HomeScreen(navController, bookDao, it)
                            }
                        }

                        composable("addBook") {
                            val currentUser = UserSession.getCurrentUser()
                            Log.d("MainActivity", "Navigating to AddBookScreen with user: $currentUser")
                            currentUser?.let {
                                AddBookScreen(navController, bookDao, it)
                            }
                        }

                        composable("user") {
                            val currentUser = UserSession.getCurrentUser()
                            Log.d("MainActivity", "Navigating to UserScreen with user: $currentUser")
                            currentUser?.let {
                                UserScreen(navController, userDao, it)
                            }
                        }
                }

                }
            }
        }
    }
}
