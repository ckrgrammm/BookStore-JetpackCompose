package com.example.bookstore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookstore.R

@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val firstName: String?,
        val lastName: String?,
        val userName: String,
        val email: String?,
        val contactNumber: String?,
        val password: String,
        val rePassword: String,
        val userStatus: Int?,
        val userProfile: String? = R.drawable.ic_default_profile.toString(),
)
{
    fun isPasswordValid(): Boolean
    {
        return password == rePassword
    }

    fun isEmailValid(): Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isContactNumberValid(): Boolean
    {
        return contactNumber!!.length >= 10
    }

    fun areFieldsNotEmpty(): Boolean
    {
        return userName.isNotBlank() &&
                password.isNotBlank() &&
                rePassword.isNotBlank()
    }
}



