package com.vanigent.meetingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vanigent.meetingapp.util.EncryptedData

@Entity(tableName = "coordinator")
data class CoordinatorEntity(
    @PrimaryKey val id: Int? = null,
    val username: String,
    val encryptedData: EncryptedData,
    val name: String?
)
