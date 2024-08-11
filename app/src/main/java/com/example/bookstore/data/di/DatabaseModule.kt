package com.example.bookstore.data.di

import android.content.Context
import androidx.room.Room
import com.example.bookstore.data.dao.CommonBookDao
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.database.AppDatabase
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
    @Actual
    fun provideActualBookDao(appDatabase: AppDatabase): CommonBookDao {
        return appDatabase.bookDao() as CommonBookDao
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}
