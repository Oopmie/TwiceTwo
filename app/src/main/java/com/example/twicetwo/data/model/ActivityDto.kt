package com.example.twicetwo.data.model

data class ActivityDto(
    val id: String? = null,
    val title: String,
    val description: String,
    val about: String,
    val organizerId: String,
    val organizerName: String,
    val startDate: String,
    val startTime: String,
    val location: String,
    val maxParticipants: Int,
    val ageRestriction: Int,
    val imageUrl: String,
    val isPro: Boolean
)