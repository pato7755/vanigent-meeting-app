package com.vanigent.meetingapp.domain.usecase

import com.vanigent.meetingapp.domain.repository.MeetingRepository
import javax.inject.Inject

class FetchMeetingsUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {

    suspend operator fun invoke(meetingId: Long) = meetingRepository.getMeeting(meetingId)

}