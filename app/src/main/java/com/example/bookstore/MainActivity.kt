package com.example.bookstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
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
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bookDao: BookDao

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Log.d("MainActivity", "Current User ${UserSession.getCurrentUser()}")

            Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute != "login" && currentRoute != "register") {
                            BottomNavigationBar(navController = navController)
                        }
                    }
            ) {
                NavHost(navController = navController, startDestination = "login") {
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
                    composable("home") {
                        UserSession.getCurrentUser()?.let { currentUser ->
                            HomeScreen(navController, bookDao, currentUser)
                        }
                    }
                    composable("addBook") {
                        UserSession.getCurrentUser()?.let { currentUser ->
                            AddBookScreen(navController, bookDao, currentUser)
                        }
                    }
                    composable("user") {
                        UserSession.getCurrentUser()?.let { currentUser ->
                            UserScreen(navController, userDao, currentUser)
                        }
                    }
                    composable("bookDetail/{bookId}") { backStackEntry ->
                        val bookId = backStackEntry.arguments?.getString("bookId")?.toInt() ?: 0
                        BookDetailScreen(navController, bookId, bookDao)
                    }
                    composable("editBook/{bookId}") { backStackEntry ->
                        val bookId = backStackEntry.arguments?.getString("bookId")?.toInt() ?: 0
                        EditBookScreen(navController, bookDao, bookId)
                    }
                }
            }
        }
    }
}
