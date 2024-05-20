package com.vanigent.meetingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanigent.meetingapp.data.local.entity.CoordinatorEntity
import com.vanigent.meetingapp.data.local.entity.MeetingEntity

@Database(
    entities = [MeetingEntity::class, CoordinatorEntity::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class MeetingDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
    abstract fun coordinatorDao(): CoordinatorDao
}