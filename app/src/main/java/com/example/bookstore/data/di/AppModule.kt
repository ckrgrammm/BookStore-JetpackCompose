package com.example.bookstore.data.di

import android.content.Context
import androidx.room.Room
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.database.AppDatabase
import com.example.bookstore.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
            @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "bookstore_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao
    {
        return database.bookDao()
    }
}
