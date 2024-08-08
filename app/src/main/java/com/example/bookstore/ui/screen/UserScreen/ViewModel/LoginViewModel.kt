package com.example.bookstore.ui.screen.UserScreen.ViewModel

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
class LoginViewModel @Inject constructor(
        private val userDao: UserDao
) : ViewModel() {
    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    var currentUser: User? = null
        private set

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userDao.getUser(userName, password)
                if (user != null) {
                    currentUser = user
                    _isLoginSuccessful.value = true
                } else {
                    _isLoginSuccessful.value = false
                    _errorMessage.value = "Invalid username or password"
                }
            } catch (e: Exception) {
                _isLoginSuccessful.value = false
                _errorMessage.value = e.message
            }
        }
    }
}
