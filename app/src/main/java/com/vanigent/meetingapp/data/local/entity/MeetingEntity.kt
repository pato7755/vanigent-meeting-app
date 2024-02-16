package com.vanigent.meetingapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "meeting", indices = [Index(value = ["id"], unique = true)])
data class MeetingEntity(

    @ColumnInfo(name = "location")
    val officeLocation: String,
    @ColumnInfo(name = "coordinator_will_consume_food")
    val coordinatorWillConsumeFood: Boolean,
    val attendees: List<AttendeeEntity>,
    val receipts: List<ReceiptEntity>,
    @PrimaryKey val id: Int? = null
)

data class AttendeeEntity(
    val attendeeFirstName: String,
    val attendeeLastName: String,
    val attendeePid: String,
    val attendeeWillConsumeFood: Boolean,
    val attendeeProfessionalDesignation: String,
    val attendeeSignature: ByteArray,
)

data class ReceiptEntity(
    val image: ByteArray,
    val vendorName: String,
    val amount: String,
    val caterer: String,
    val date: String,
)
