package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.data.CoordinatorDao
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
import com.vanigent.meetingapp.data.local.entity.CoordinatorEntity
import com.vanigent.meetingapp.data.mapper.AttendeeMapper
import com.vanigent.meetingapp.data.mapper.MeetingMapper
import com.vanigent.meetingapp.data.remote.RemoteApi
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.repository.CryptoManager
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.util.Constants.LOGIN_URL
import com.vanigent.meetingapp.util.EncryptedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDao: MeetingDao,
    private val coordinatorDao: CoordinatorDao,
    private val remoteApi: RemoteApi,
    private val database: MeetingDatabase,
    private val cryptoManager: CryptoManager
) : MeetingRepository {
    override fun dbSetup() {
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
        println("saveMeeting - ${meeting.address}")
        meeting.receipt.map {
            println(it.receiptItems)
        }
        val mappedMeeting = MeetingMapper.mapToEntity(meeting)
        mappedMeeting.receipts.map {
            println("mappedMeeting - ${it.receiptItems}")
        }
        return meetingDao.insertMeeting(MeetingMapper.mapToEntity(meeting))
    }

    override suspend fun addAttendeeToMeeting(meetingId: Long, attendee: Attendee) {
        val meeting = meetingDao.getMeetingById(meetingId.toString())
        val updatedAttendee = meeting?.attendees?.toMutableList()?.apply {
            add(AttendeeMapper.mapToEntity(attendee))
        }

        val updatedMeeting = updatedAttendee?.let {
            meeting.copy(
                attendees = it
            )
        }

        updatedMeeting?.let { meetingDao.updateMeeting(it) }
    }

    override suspend fun getMeetings(): Flow<WorkResult<List<Meeting>>> = flow {
        emit(WorkResult.Loading())
        val allMeetings =
            meetingDao.getAllMeetings().map { MeetingMapper.mapToDomainWithoutImages(it) }
        emit(WorkResult.Success(allMeetings))
    }

    override suspend fun getMeeting(meetingId: Long): Flow<WorkResult<Meeting?>> = flow {
        emit(WorkResult.Loading())
        val meeting = meetingDao.getMeetingById(meetingId.toString())
            ?.let { MeetingMapper.mapToDomainWithoutImages(it) }
        emit(WorkResult.Success(meeting))
    }

    override suspend fun login(coordinator: Coordinator): WorkResult<Coordinator> = try {
        val response = remoteApi.login(LOGIN_URL, coordinator)
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                val fullName = responseBody.fullName

                val encryptedPassword = cryptoManager.encryptWithGeneratedIV(password = coordinator.password)

                coordinatorDao.deleteLoginCredentials()

                coordinatorDao.saveCoordinatorDetails(
                    CoordinatorEntity(
                        username = coordinator.username,
                        encryptedData = encryptedPassword,
                        name = fullName
                    )
                )

                WorkResult.Success(
                    Coordinator(
                        username = coordinator.username,
                        password = coordinator.password,
                        fullName = fullName
                    )
                )
//                WorkResult.Success(fullName)
            } else {
                WorkResult.Error(message = "Empty response body")
            }
        } else {
            WorkResult.Error(message = "This user does not exist")
        }
    } catch (ex: Exception) {
        Timber.e(ex.localizedMessage)
        WorkResult.Error("An error occurred while logging in")
    }

    override suspend fun authenticateCoordinator(userPassword: String): Boolean {
        val storedData = coordinatorDao.fetchCoordinatorDetails().encryptedData
        val iv = storedData.iv

        var userDataEntry: EncryptedData

        iv?.let {
            userDataEntry = cryptoManager.encrypt(userPassword, iv)
            return userDataEntry.password.trim() == storedData.password.trim()
        }
        return false
    }
}