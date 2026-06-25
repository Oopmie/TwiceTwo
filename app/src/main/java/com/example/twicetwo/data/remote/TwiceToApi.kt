package com.example.twicetwo.data.remote

import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.data.model.InterestDto
import com.example.twicetwo.data.model.ParticipantDto
import com.example.twicetwo.data.model.UserDto
import retrofit2.http.*

interface TwiceToApi {

    // USERS
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): UserDto

    @POST("users")
    suspend fun register(
        @Body user: UserDto
    ): UserDto

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: UserDto
    ): UserDto


    // ACTIVITIES
    @GET("activities")
    suspend fun getActivities(): List<ActivityDto>

    @GET("activities/{id}")
    suspend fun getActivity(
        @Path("id") id: String
    ): ActivityDto

    @POST("activities")
    suspend fun createActivity(
        @Body activity: ActivityDto
    ): ActivityDto

    @PUT("activities/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body activity: ActivityDto
    ): ActivityDto

    // PARTICIPANTS
    @GET("participants")
    suspend fun getParticipants(): List<ParticipantDto>

    @POST("participants")
    suspend fun createParticipant(
        @Body participant: ParticipantDto
    ): ParticipantDto

    @POST("participants")
    suspend fun joinActivity(
        @Body participant: ParticipantDto
    ): ParticipantDto


    // INTERESTS
    @GET("interests")
    suspend fun getInterests(): List<InterestDto>
}