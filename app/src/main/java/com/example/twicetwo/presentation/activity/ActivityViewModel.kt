package com.example.twicetwo.presentation.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.data.model.ParticipantDto
import com.example.twicetwo.domain.repository.TwiceRepository
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val repository: TwiceRepository
) : ViewModel() {

    var participantsUsernames = mutableStateOf<List<String>>(emptyList())
        private set
    fun loadParticipants(activityId: String) {
        viewModelScope.launch {

            val participants = repository.getParticipants(activityId)
            val users = repository.getUsers()

            val usernames = participants.mapNotNull { participant ->
                users.find { it.id == participant.userId }?.username
            }

            participantsUsernames.value = usernames
        }
    }

    var selectedActivity by mutableStateOf<ActivityDto?>(null)
        private set

    var participants by mutableStateOf<List<ParticipantDto>>(emptyList())
        private set

    fun loadActivity(
        id: String
    ) {
        viewModelScope.launch {
            selectedActivity = repository.getActivities().find {
                it.id == id
            }
        }
    }


    fun createActivity(
        activity: ActivityDto,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.createActivity(activity)
            onSuccess()
        }
    }

    fun joinActivity(
        activityId: String,
        userId: String
    ) {
        viewModelScope.launch {

            val alreadyJoined = participants.any {
                it.userId == userId
            }

            if (!alreadyJoined) {

                repository.createParticipant(
                    ParticipantDto(
                        activityId = activityId,
                        userId = userId
                    )
                )

                loadParticipants(activityId)
            }
        }
    }
}