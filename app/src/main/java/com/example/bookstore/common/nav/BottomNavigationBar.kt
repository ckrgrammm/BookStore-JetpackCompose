package com.example.bookstore.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import com.example.bookstore.common.nav.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
            BottomNavItem("Books", "home", Icons.Default.Email),
            BottomNavItem("Add", "addBook", Icons.Default.Add),
            BottomNavItem("User", "user", Icons.Default.Person)
    )
    BottomNavigation(
            backgroundColor = Color.Black,
            contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                    label = { Text(text = item.name, fontSize = 9.sp) },
                    selected = currentRoute == item.route,
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            )
        }
    }
}
