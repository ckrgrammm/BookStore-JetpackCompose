package com.example.bookstore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "books")
data class Book(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val title: String,
        val author: String,
        val publishedYear: Int?,
        val description: String?,
        val imageUri: String? = null,
        val dateOfRegister: Date,
        val bookOwnerId: Int
) {
    fun isTitleValid(): Boolean {
        return title.isNotBlank()
    }

    fun isAuthorValid(): Boolean {
        return author.isNotBlank()
    }

    fun isPublishedYearValid(): Boolean {
        return publishedYear != null && publishedYear > 1950 && publishedYear <= 2024
    }

    fun isDescriptionValid(): Boolean {
        return description?.isNotBlank() ?: true
    }

    fun areFieldsNotEmpty(): Boolean {
        return title.isNotBlank() && author.isNotBlank() && publishedYear != null
    }
}
