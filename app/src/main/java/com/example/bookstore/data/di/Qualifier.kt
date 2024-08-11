package com.example.bookstore.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActualUserDao

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockUserDao

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActualBookDao

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockBookDao

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Actual