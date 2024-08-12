package com.example.bookstore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.data.model.User

@Dao
interface UserDao : IUserDao {
    @Insert
    override suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE userName = :userName AND password = :password")
    override suspend fun getUser(userName: String, password: String): User?

    @Update
    override suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE userName = :userName")
    override suspend fun getUserByUsername(userName: String): User?
}
