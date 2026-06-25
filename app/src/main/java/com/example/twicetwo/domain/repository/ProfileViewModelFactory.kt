package com.example.twicetwo.domain.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twicetwo.presentation.profile.ProfileViewModel

class ProfileViewModelFactory(
    private val repository: TwiceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repository) as T
    }
}