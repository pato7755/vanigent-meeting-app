package com.vanigent.meetingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordinator")
data class CoordinatorEntity(
    @PrimaryKey val id: Int? = null,
    val username: String,
    val password: String,
    val name: String?
)