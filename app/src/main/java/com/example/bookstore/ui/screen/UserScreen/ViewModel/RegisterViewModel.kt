package com.example.bookstore.ui.screen.UserScreen.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.model.User

@HiltViewModel
class RegisterViewModel @Inject constructor(
        private val userDao: UserDao
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(user: User) {
        viewModelScope.launch {
            try {
                userDao.insertUser(user)
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.d("RegisterViewModel", "Error Msg ${e.message}")
            }
        }
    }

    suspend fun isUsernameTaken(userName: String): Boolean {
        return userDao.getUserByUsername(userName) != null
    }
}
