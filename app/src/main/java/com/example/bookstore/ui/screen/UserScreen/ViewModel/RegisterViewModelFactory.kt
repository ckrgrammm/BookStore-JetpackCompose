package com.example.bookstore.ui.screen.UserScreen.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.data.dao.UserDao


class RegisterViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
