package com.vanigent.meetingapp.domain.usecase

import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import javax.inject.Inject

class SaveAttendeeUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {

    suspend operator fun invoke(meeting: Meeting) =
        meetingRepository.saveMeeting(meeting = meeting)

}