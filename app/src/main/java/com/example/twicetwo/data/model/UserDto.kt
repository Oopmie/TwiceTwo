package com.example.twicetwo.data.model

data class UserDto(
    val id: String? = null,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val avatar: String? = null,
    val firstName: String = "",
    val age: Int = 16,
    val city: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
    val hasSubscription: Boolean = false,
    val isActive: Boolean = true
)