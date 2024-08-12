package com.example.bookstore.data.mock

import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.data.model.User

class MockUserDaoClass : IUserDao {

    private val mockUser = User(
            userName = "Testing",
            password = "password",
            rePassword = "password",
            contactNumber = "0123456789",
            userProfile = null,
            userStatus = null,
            email = "testing@gmail.com",
            id = 1,
            firstName = "test",
            lastName = "ing"
    )

    override suspend fun insertUser(user: User): Long {
        return 1L
    }

    override suspend fun getUser(userName: String, password: String): User? {
        return if (userName == mockUser.userName && password == mockUser.password) {
            mockUser
        } else {
            null
        }
    }

    override suspend fun updateUser(user: User) {
    }

    override suspend fun getUserByUsername(userName: String): User? {
        return if (userName == mockUser.userName) {
            mockUser
        } else {
            null
        }
    }
}
