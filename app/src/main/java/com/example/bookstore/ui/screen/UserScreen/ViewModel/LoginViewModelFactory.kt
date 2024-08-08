package com.example.bookstore.ui.screen.UserScreen.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.data.dao.UserDao

class LoginViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
