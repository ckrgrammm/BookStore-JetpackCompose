package com.example.bookstore.data.di

import android.content.Context
import androidx.room.Room
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.dao.CommonBookDao
import com.example.bookstore.data.dao.IBookDao
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.database.AppDatabase
import com.example.bookstore.data.model.Book
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "bookstore_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideBookDao(appDatabase: AppDatabase): BookDao
    {
        return appDatabase.bookDao()
    }

    @ActualBookDao
    @Provides
    @Singleton
    fun provideActualBookDao(appDatabase: AppDatabase): BookDao {
        return appDatabase.bookDao()
    }

//    @MockBookDao
//    @Provides
//    fun provideMockBookDao(bookDao: BookDao): IBookDao {
//        return object : IBookDao by bookDao {
//            override suspend fun insertBook(book: Book): Long {
//                return -1L
//            }
//        }
//    }

    @MockBookDao
    @Provides
    fun provideMockBookDao(): IBookDao {
        return MockBookDao()
    }


    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}
