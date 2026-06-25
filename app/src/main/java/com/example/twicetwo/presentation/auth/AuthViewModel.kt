package com.example.twicetwo.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.domain.repository.TwiceRepository
import com.example.twicetwo.data.model.UserDto
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: TwiceRepository
) : ViewModel() {

    fun login(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val users = repository.getUsers()

            val user = users.find {
                it.email == email && it.password == password
            }

            if (user != null) {
                SessionManager.login(context, user)
                onSuccess()
            } else {
                onError()
            }
        }
    }

    fun register(
        context: Context,
        username: String,
        email: String,
        password: String,
        firstName: String,
        age: Int,
        city: String,
        gender: String,
        phoneNumber: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {

            val user = UserDto(
                username = username,
                email = email,
                password = password,
                avatar = "",
                firstName = firstName,
                age = age,
                city = city,
                gender = gender,
                phoneNumber = phoneNumber,
                hasSubscription = false,
                isActive = true
            )

            val createdUser = repository.register(user)

            SessionManager.login(context, createdUser)

            onSuccess()
        }
    }
}