package com.example.bookstore.data.repository

import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.di.ActualUserDao
import com.example.bookstore.data.di.MockUserDao
import com.example.bookstore.data.model.User
import javax.inject.Inject

interface UserRepository {
        suspend fun insertUser(user: User): Long
        suspend fun getUser(userName: String, password: String): User?
        suspend fun updateUser(user: User)
        suspend fun getUserByUsername(userName: String): User?
}