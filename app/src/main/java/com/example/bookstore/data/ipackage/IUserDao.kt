package com.example.bookstore.data.ipackage

import com.example.bookstore.data.model.User

interface IUserDao
{
    suspend fun insertUser(user: User): Long
    suspend fun getUser(userName: String, password: String): User?
    suspend fun updateUser(user: User)
    suspend fun getUserByUsername(userName: String): User?
}