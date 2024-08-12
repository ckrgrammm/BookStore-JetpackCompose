package com.example.bookstore.data.dao

import com.example.bookstore.data.model.Book
import java.util.Date


class MockBookDaoClass : IBookDao {
    override suspend fun insertBook(book: Book): Long {
        return -1L
    }

    override suspend fun getAllBooks(): List<Book> {
        return emptyList()
    }

    override suspend fun getBookById(bookId: Int): Book {
        return Book(
                id = 0,
                title = "Mock Book",
                author = "Mock Author",
                publishedYear = 2023,
                description = "Testing purposesssssss",
                bookOwnerId = 1,
                dateOfRegister = Date(),
                imageUri = null
        )
    }

    override suspend fun deleteBook(book: Book) {
    }

    override suspend fun getAllBooksSortedByDate(): List<Book> {
        return emptyList()
    }

    override suspend fun updateBook(book: Book) {
    }
}
