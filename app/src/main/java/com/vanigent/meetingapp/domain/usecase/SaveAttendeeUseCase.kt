package com.vanigent.meetingapp.domain.usecase

import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import javax.inject.Inject

class SaveAttendeeUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {

    suspend operator fun invoke(meetingId: Long, attendee: Attendee) =
        meetingRepository.addAttendeeToMeeting(
            meetingId = meetingId,
            attendee = attendee,
        )

}