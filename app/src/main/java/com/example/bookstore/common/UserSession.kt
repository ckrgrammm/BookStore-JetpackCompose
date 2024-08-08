package com.example.bookstore.common

import com.example.bookstore.data.model.User

object UserSession {
    private var currentUser: User? = null

    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun getCurrentUserId(): Int? {
        return currentUser?.id
    }
}
