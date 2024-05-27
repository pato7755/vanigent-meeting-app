package com.vanigent.meetingapp.domain.repository

import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.domain.model.Meeting
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.File

interface MeetingRepository {

    fun dbSetup()

    fun getProfessionalDesignations(): List<String>

    suspend fun saveMeeting(meeting: Meeting): Long

    suspend fun addAttendeeToMeeting(meetingId: Long, attendee: Attendee)

    suspend fun getMeetings(): Flow<WorkResult<List<Meeting>>>

    suspend fun getMeeting(meetingId: Long): Flow<WorkResult<Meeting?>>

    suspend fun login(coordinator: Coordinator): WorkResult<Coordinator>

    suspend fun authenticateCoordinator(userPassword: String): Boolean

    suspend fun uploadPDFToServer(file: File): Boolean

}