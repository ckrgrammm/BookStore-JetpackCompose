package com.example.bookstore.data.repository

import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.di.ActualUserDao
import com.example.bookstore.data.di.MockUserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
        @ActualUserDao private val actualUserDao: UserDao,
        @MockUserDao private val mockUserDao: UserDao
) {
}