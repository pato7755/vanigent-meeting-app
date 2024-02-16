package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
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
        // ("Not yet implemented")
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
        return dao.upsertMeeting(MeetingMapper.mapToEntity(meeting))
    }

    override suspend fun addAttendeeToMeeting(meetingId: Long, attendee: Attendee) {
        TODO("Not yet implemented")
    }

    override suspend fun getMeetings(): Flow<WorkResult<List<Meeting>>> = flow {
        emit(WorkResult.Loading())
        val allMeetings = dao.getAllMeetings().map { MeetingMapper.mapToDomain(it) }
        emit(WorkResult.Success(allMeetings))
    }
}