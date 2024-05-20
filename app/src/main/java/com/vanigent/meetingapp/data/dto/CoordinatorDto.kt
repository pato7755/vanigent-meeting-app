package com.vanigent.meetingapp.data.dto

import com.google.gson.annotations.SerializedName

data class CoordinatorDto(
    @SerializedName(value = "fullname")
    val fullName: String
)
