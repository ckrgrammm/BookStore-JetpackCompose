package com.example.bookstore.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bookstore.data.model.Book

@Dao
interface BookDao : IBookDao {
    @Insert
    override suspend fun insertBook(book: Book): Long

    @Query("SELECT * FROM books")
    override suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE id = :bookId")
    override suspend fun getBookById(bookId: Int): Book

    @Delete
    override suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books ORDER BY dateOfRegister DESC")
    override suspend fun getAllBooksSortedByDate(): List<Book>

    @Update
    override suspend fun updateBook(book: Book)
}
