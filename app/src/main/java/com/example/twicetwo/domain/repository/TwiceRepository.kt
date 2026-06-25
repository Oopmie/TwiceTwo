package com.example.twicetwo.domain.repository

import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.data.model.ParticipantDto
import com.example.twicetwo.data.model.UserDto

interface TwiceRepository {

    suspend fun getUsers(): List<UserDto>

    suspend fun register(user: UserDto): UserDto

    suspend fun updateUser(id: String, user: UserDto): UserDto

    suspend fun getActivities(): List<ActivityDto>

    suspend fun getActivity(id: String): ActivityDto

    suspend fun createActivity(activity: ActivityDto): ActivityDto

    suspend fun updateActivity(id: String, activity: ActivityDto): ActivityDto

    suspend fun joinActivity(participant: ParticipantDto): ParticipantDto

    suspend fun getParticipants(): List<ParticipantDto>

    suspend fun createParticipant(participant: ParticipantDto): ParticipantDto
}