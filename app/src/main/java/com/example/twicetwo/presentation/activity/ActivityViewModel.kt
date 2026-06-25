package com.example.twicetwo.presentation.activity

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

    fun loadParticipants(
        activityId: String
    ) {
        viewModelScope.launch {
            participants = repository.getParticipants().filter {
                it.activityId == activityId
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