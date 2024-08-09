package com.example.bookstore.data.di

import android.content.Context
import androidx.room.Room
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ActualUserDao
    @Provides
    fun provideActualUserDao(@ApplicationContext context: Context): UserDao {
        val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "actual_database"
        ).build()
        return db.userDao()
    }

    @MockUserDao
    @Provides
    fun provideMockUserDao(@ApplicationContext context: Context): UserDao {
        val db = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
        ).build()
        return db.userDao()
    }

    @ActualBookDao
    @Provides
    fun provideActualBookDao(@ApplicationContext context: Context): BookDao {
        val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "actual_database"
        ).build()
        return db.bookDao()
    }

    @MockBookDao
    @Provides
    fun provideMockBookDao(@ApplicationContext context: Context): BookDao {
        val db = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
        ).build()
        return db.bookDao()
    }
}
