package com.vanigent.meetingapp.domain.repository

import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Meeting
import kotlinx.coroutines.flow.Flow

interface MeetingRepository {

    fun login()

    fun getProfessionalDesignations(): List<String>

    suspend fun saveMeeting(meeting: Meeting): Long

    suspend fun addAttendeeToMeeting(meetingId: Long, attendee: Attendee)

    suspend fun getMeetings(): Flow<WorkResult<List<Meeting>>>

}