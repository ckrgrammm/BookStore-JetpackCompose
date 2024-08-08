package com.example.bookstore.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bookstore.data.model.Book

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(book: Book): Long

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): Book

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books ORDER BY dateOfRegister DESC")
    suspend fun getAllBooksSortedByDate(): List<Book>

    @Update
    suspend fun updateBook(book: Book)
}
