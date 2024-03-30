package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.AttendeeEntity
import com.vanigent.meetingapp.domain.model.Attendee

object AttendeeMapper {
    fun mapToEntity(attendee: Attendee): AttendeeEntity {
        return AttendeeEntity(
            attendeeFirstName = attendee.attendeeFirstName,
            attendeeLastName = attendee.attendeeLastName,
            attendeePid = attendee.attendeePid,
            attendeeWillConsumeFood = attendee.attendeeWillConsumeFood,
            attendeeProfessionalDesignation = attendee.attendeeProfessionalDesignation,
            attendeeSignature = attendee.attendeeSignature
        )
    }

    fun mapToDomain(attendeeEntity: AttendeeEntity): Attendee {
        return Attendee(
            attendeeFirstName = attendeeEntity.attendeeFirstName,
            attendeeLastName = attendeeEntity.attendeeLastName,
            attendeePid = attendeeEntity.attendeePid,
            attendeeWillConsumeFood = attendeeEntity.attendeeWillConsumeFood,
            attendeeProfessionalDesignation = attendeeEntity.attendeeProfessionalDesignation,
            attendeeSignature = attendeeEntity.attendeeSignature
        )
    }
}