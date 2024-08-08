package com.example.bookstore.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookstore.common.Converters
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.model.User
import com.example.bookstore.data.model.Book

@Database(entities = [User::class, Book::class], version = 7)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
}
