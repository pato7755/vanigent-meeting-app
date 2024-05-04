package com.vanigent.meetingapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "meeting", indices = [Index(value = ["id"], unique = true)])
data class MeetingEntity(

    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "location")
    val address: AddressEntity,
    @ColumnInfo(name = "coordinator_will_consume_food")
    val coordinatorWillConsumeFood: Boolean,
    val attendees: List<AttendeeEntity>,
    val receipts: List<ReceiptEntity>
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
    val receiptItems: MutableMap<String, String>,
    val receiptImagePath: String?
)

data class AddressEntity(
    val physicianName: String,
    val officeName: String,
    val lineOne: String,
    val city: String,
    val state: String,
    val country: String
)