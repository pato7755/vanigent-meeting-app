package com.vanigent.meetingapp.domain.model

data class Coordinator(
    val username: String,
    val password: String,
    val fullName: String? = null
)
