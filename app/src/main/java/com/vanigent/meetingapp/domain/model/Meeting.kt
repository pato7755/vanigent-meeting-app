package com.vanigent.meetingapp.domain.model

data class Meeting(
    val id: Long? = null,
    val address: Address,
    val coordinatorWillConsumeFood: Boolean,
    val receipt: List<Receipt>,
    val attendee: List<Attendee>
)

data class Attendee(
    val attendeeFirstName: String,
    val attendeeLastName: String,
    val attendeePid: String,
    val attendeeWillConsumeFood: Boolean,
    val attendeeProfessionalDesignation: String,
    val attendeeSignature: ByteArray,
)

data class Receipt(
    val receiptItems: MutableMap<String, String>,
    val receiptImagePath: String? = null
)

data class Address(
    val physicianName: String,
    val officeName: String,
    val lineOne: String,
    val city: String,
    val state: String,
    val country: String
)