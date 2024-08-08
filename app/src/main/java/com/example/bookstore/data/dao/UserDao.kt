package com.example.bookstore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bookstore.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE userName = :userName AND password = :password")
    suspend fun getUser(userName: String, password: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE userName = :userName")
    suspend fun getUserByUsername(userName: String): User?
}
