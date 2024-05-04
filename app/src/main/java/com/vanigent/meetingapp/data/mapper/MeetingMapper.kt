package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.AddressEntity
import com.vanigent.meetingapp.data.local.entity.AttendeeEntity
import com.vanigent.meetingapp.data.local.entity.MeetingEntity
import com.vanigent.meetingapp.data.local.entity.ReceiptEntity
import com.vanigent.meetingapp.domain.model.Address
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.model.Receipt

object MeetingMapper {
    fun mapToEntity(meeting: Meeting): MeetingEntity {
        return MeetingEntity(
            id = meeting.id,
            address = AddressEntity(
                physicianName = meeting.address.physicianName,
                officeName = meeting.address.officeName,
                lineOne = meeting.address.lineOne,
                city = meeting.address.city,
                state = meeting.address.state,
                country = meeting.address.country
            ),
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
                    receiptImagePath = it.receiptImagePath,
                    receiptItems = it.receiptItems
                )
            }
        )
    }

    fun mapToDomain(meetingEntity: MeetingEntity): Meeting {
        return Meeting(
            id = meetingEntity.id ?: 0L,
            address = Address(
                physicianName = meetingEntity.address.physicianName,
                officeName = meetingEntity.address.officeName,
                lineOne = meetingEntity.address.lineOne,
                city = meetingEntity.address.city,
                state = meetingEntity.address.state,
                country = meetingEntity.address.country
            ),
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
                    receiptImagePath = it.receiptImagePath,
                    receiptItems = it.receiptItems
                )
            }
        )
    }

    fun mapToDomainWithoutImages(meetingEntity: MeetingEntity): Meeting {
        return Meeting(
            id = meetingEntity.id ?: 0L,
            address = Address(
                physicianName = meetingEntity.address.physicianName,
                officeName = meetingEntity.address.officeName,
                lineOne = meetingEntity.address.lineOne,
                city = meetingEntity.address.city,
                state = meetingEntity.address.state,
                country = meetingEntity.address.country
            ),
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
                    receiptImagePath = it.receiptImagePath,
                    receiptItems = it.receiptItems
                )
            }
        )
    }
}