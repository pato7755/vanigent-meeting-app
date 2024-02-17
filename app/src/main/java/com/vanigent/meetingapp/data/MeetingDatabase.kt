package com.vanigent.meetingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanigent.meetingapp.data.local.entity.MeetingEntity

@Database(
    entities = [MeetingEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MeetingDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
}