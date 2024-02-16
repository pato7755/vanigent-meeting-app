package com.vanigent.meetingapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vanigent.meetingapp.data.local.entity.MeetingEntity

/**
 * Data Access Object for the Meeting table.
 */
@Dao
interface MeetingDao {

    /**
     * Insert if it doesn't exist or ignore if it does
     *
     * @param meeting
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsertMeeting(meeting: MeetingEntity)

    /**
     * Get meeting by id
     *
     * @param id
     * @return local meeting
     */
    @Query("SELECT * FROM Meeting WHERE id = :id")
    suspend fun getMeetingById(id: String): MeetingEntity?

    /**
     * Get all meetings
     *
     * @return list of local meetings
     */
    @Query("SELECT * FROM Meeting")
    suspend fun getAllMeetings(): List<MeetingEntity>

}
