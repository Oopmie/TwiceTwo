package com.example.twicetwo.presentation.profile

import android.content.Context
import com.example.twicetwo.data.local.SessionManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.twicetwo.data.model.UserDto
import com.example.twicetwo.domain.repository.TwiceRepository

class ProfileViewModel(
    private val repository: TwiceRepository
) : ViewModel() {

    var user by mutableStateOf<UserDto?>(null)
        private set

    fun loadUser(context: Context) {
        viewModelScope.launch {
            val users = repository.getUsers()
            SessionManager.restore(context, users)
            user = SessionManager.currentUser
        }
    }

    fun updateProfile(updatedUser: UserDto, onSuccess: (UserDto) -> Unit) {
        viewModelScope.launch {
            try {

                println("CLICKED UPDATE")

                val result = repository.updateUser(
                    updatedUser.id ?: "",
                    updatedUser
                )

                println("UPDATE SUCCESS = $result")

                user = result
                SessionManager.currentUser = result

                onSuccess(result)

            } catch (e: Exception) {
                println("UPDATE ERROR = ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateAvatar(uri: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {

            val current = user ?: return@launch

            val result = repository.updateUser(
                current.id!!,
                current.copy(avatar = uri)
            )

            SessionManager.currentUser = result
            user = result

            onSuccess()
        }
    }

    fun buySubscription(onSuccess: () -> Unit) {
        viewModelScope.launch {

            val current = user ?: return@launch

            val result = repository.updateUser(
                current.id!!,
                current.copy(hasSubscription = true)
            )

            SessionManager.currentUser = result
            user = result

            onSuccess()
        }
    }
}