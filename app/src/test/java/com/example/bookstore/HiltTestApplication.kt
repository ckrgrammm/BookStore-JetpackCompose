package com.example.bookstore

import android.app.Application
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BookstoreApplication::class)
class HiltTestApplication : Application()