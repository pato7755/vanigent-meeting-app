package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.AttendeeEntity
import com.vanigent.meetingapp.data.local.entity.MeetingEntity
import com.vanigent.meetingapp.data.local.entity.ReceiptEntity
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.model.Receipt

object MeetingMapper {
    fun mapToEntity(meeting: Meeting): MeetingEntity {
        return MeetingEntity(
            officeLocation = meeting.officeLocation,
            coordinatorWillConsumeFood = meeting.coordinatorWillConsumeFood,
            attendees = meeting.attendee.map {
                AttendeeEntity(
                    attendeeFirstName = it.attendeeFirstName,
                    attendeeLastName = it.attendeeLastName,
                    attendeePid = it.attendeePid,
                    attendeeWillConsumeFood = it.attendeeWillConsumeFood,
                    attendeeProfessionalDesignation = it.attendeeProfessionalDesignation,
                    attendeeSignature = it.attendeeSignature
                )
            },
            receipts = meeting.receipt.map {
                ReceiptEntity(
                    image = it.image,
                    vendorName = it.vendorName,
                    amount = it.amount,
                    caterer = it.caterer,
                    date = it.date
                )
            }
        )
    }

    fun mapToDomain(meetingEntity: MeetingEntity): Meeting {
        return Meeting(
            officeLocation = meetingEntity.officeLocation,
            coordinatorWillConsumeFood = meetingEntity.coordinatorWillConsumeFood,
            attendee = meetingEntity.attendees.map {
                Attendee(
                    attendeeFirstName = it.attendeeFirstName,
                    attendeeLastName = it.attendeeLastName,
                    attendeePid = it.attendeePid,
                    attendeeWillConsumeFood = it.attendeeWillConsumeFood,
                    attendeeProfessionalDesignation = it.attendeeProfessionalDesignation,
                    attendeeSignature = it.attendeeSignature
                )
            },
            receipt = meetingEntity.receipts.map {
                Receipt(
                    image = it.image,
                    vendorName = it.vendorName,
                    amount = it.amount,
                    caterer = it.caterer,
                    date = it.date
                )
            }
        )
    }
}