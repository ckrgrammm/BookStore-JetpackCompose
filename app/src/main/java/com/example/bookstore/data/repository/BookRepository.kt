package com.example.bookstore.data.repository

import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.di.ActualBookDao
import com.example.bookstore.data.di.MockBookDao
import com.example.bookstore.data.model.Book
import javax.inject.Inject

interface BookRepository {
        suspend fun insertBook(book: Book): Long
        suspend fun getAllBooks(): List<Book>
        suspend fun getBookById(bookId: Int): Book
        suspend fun deleteBook(book: Book)
        suspend fun getAllBooksSortedByDate(): List<Book>
        suspend fun updateBook(book: Book)
}