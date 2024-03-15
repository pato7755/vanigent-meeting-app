package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
import com.vanigent.meetingapp.data.mapper.AttendeeMapper
import com.vanigent.meetingapp.data.mapper.MeetingMapper
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val dao: MeetingDao,
    private val database: MeetingDatabase
) : MeetingRepository {
    override fun login() {
        if (!database.isOpen) {
            Timber.d("Opening database...")
            database.openHelper.writableDatabase
            Timber.d("Database opened successfully.")
        }

    }

    override fun getProfessionalDesignations(): List<String> {
        return listOf(
            "MD",
            "DO",
            "PA",
            "NP",
            "PharmD",
            "RPh",
            "RN",
            "LPN",
            "MA",
            "Office Staff"
        )
    }

    override suspend fun saveMeeting(meeting: Meeting): Long {
        println("saveMeeting - ${meeting.officeLocation}")
        meeting.receipt.map {
            println(it.receiptItems)
        }
        val mappedMeeting = MeetingMapper.mapToEntity(meeting)
        mappedMeeting.receipts.map {
            println("mappedMeeting - ${it.receiptItems}")
        }
        return dao.insertMeeting(MeetingMapper.mapToEntity(meeting))
    }

    override suspend fun addAttendeeToMeeting(meetingId: Long, attendee: Attendee) {
        val meeting = dao.getMeetingById(meetingId.toString())
        val updatedAttendee = meeting?.attendees?.toMutableList()?.apply {
            add(AttendeeMapper.mapToEntity(attendee))
        }

        val updatedMeeting = updatedAttendee?.let {
            meeting.copy(
                attendees = it
            )
        }

        updatedMeeting?.let { dao.updateMeeting(it) }
    }

    override suspend fun getMeetings(): Flow<WorkResult<List<Meeting>>> = flow {
        emit(WorkResult.Loading())
        val allMeetings = dao.getAllMeetings().map { MeetingMapper.mapToDomainWithoutImages(it) }
        emit(WorkResult.Success(allMeetings))
    }

    override suspend fun getMeeting(meetingId: Long): Flow<WorkResult<Meeting?>> = flow {
        emit(WorkResult.Loading())
        val meeting = dao.getMeetingById(meetingId.toString())
            ?.let { MeetingMapper.mapToDomainWithoutImages(it) }
        emit(WorkResult.Success(meeting))

    }
}