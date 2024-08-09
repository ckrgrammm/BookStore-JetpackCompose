package com.example.bookstore.data.repository

import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.di.ActualBookDao
import com.example.bookstore.data.di.MockBookDao
import javax.inject.Inject

class BookRepository @Inject constructor(
        @ActualBookDao private val actualBookDao: BookDao,
        @MockBookDao private val mockBookDao: BookDao
) {
}