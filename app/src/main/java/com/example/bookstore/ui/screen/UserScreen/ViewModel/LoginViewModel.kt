package com.example.bookstore.ui.screen.UserScreen.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.di.ActualUserDao
import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.data.model.User

@HiltViewModel
class LoginViewModel @Inject constructor(
        @ActualUserDao private val userDao: IUserDao
) : ViewModel()
{
    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    var currentUser: User? = null
        private set

    fun login(userName: String, password: String)
    {
        viewModelScope.launch {
            try
            {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(userName)
                }

                if (user != null)
                {
                    if (user.password == password)
                    {
                        currentUser = user
                        _isLoginSuccessful.value = true
                    }
                    else
                    {
                        _isLoginSuccessful.value = false
                        _errorMessage.value = "Invalid password"
                    }
                }
                else
                {
                    _isLoginSuccessful.value = false
                    _errorMessage.value = "No account found with this username"
                }
            }
            catch (e: Exception)
            {
                _isLoginSuccessful.value = false
                _errorMessage.value = e.message
            }
        }
    }


    fun clearErrorMessage()
    {
        _errorMessage.value = null
    }
}
