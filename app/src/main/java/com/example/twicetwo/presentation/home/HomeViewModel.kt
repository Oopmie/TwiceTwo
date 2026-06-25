package com.example.twicetwo.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.domain.repository.TwiceRepository

class HomeViewModel(
    private val repository: TwiceRepository
) : ViewModel() {

    fun loadBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        if (uri == null) return null

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    var activities by mutableStateOf<List<ActivityDto>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    init {
        loadActivities()
    }

    fun loadActivities() {
        viewModelScope.launch {
            activities = repository.getActivities()
        }
    }

    fun updateSearch(query: String) {
        searchQuery = query
    }

    fun filteredActivities(): List<ActivityDto> {
        return if (searchQuery.isBlank()) {
            activities
        } else {
            activities.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
        }
    }
}