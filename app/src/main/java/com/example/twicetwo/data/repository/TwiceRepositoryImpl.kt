package com.example.twicetwo.data.repository

import com.example.twicetwo.domain.repository.TwiceRepository
import com.example.twicetwo.data.remote.TwiceToApi
import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.data.model.ParticipantDto
import com.example.twicetwo.data.model.UserDto

class TwiceRepositoryImpl(
    private val api: TwiceToApi
) : TwiceRepository {

    override suspend fun getUsers() = api.getUsers()

    override suspend fun register(user: UserDto): UserDto {
        return api.register(user)
    }

    override suspend fun updateUser(
        id: String,
        user: UserDto
    ): UserDto {
        return api.updateUser(id, user)
    }

    override suspend fun getActivities() =
        api.getActivities()

    override suspend fun getActivity(id: String) =
        api.getActivity(id)

    override suspend fun createActivity(activity: ActivityDto) =
        api.createActivity(activity)

    override suspend fun updateActivity(id: String, activity: ActivityDto)=
        api.updateActivity(id, activity)

    override suspend fun joinActivity(participant: ParticipantDto) =
        api.joinActivity(participant)

    override suspend fun getParticipants(): List<ParticipantDto> {
        return api.getParticipants()
    }

    override suspend fun createParticipant(
        participant: ParticipantDto
    ): ParticipantDto {
        return api.createParticipant(participant)
    }
}