package com.example.bookstore.data.ipackage

import com.example.bookstore.data.model.Book

interface IBookDao {
    suspend fun insertBook(book: Book): Long
    suspend fun getAllBooks(): List<Book>
    suspend fun getBookById(bookId: Int): Book
    suspend fun deleteBook(book: Book)
    suspend fun getAllBooksSortedByDate(): List<Book>
    suspend fun updateBook(book: Book)
}
