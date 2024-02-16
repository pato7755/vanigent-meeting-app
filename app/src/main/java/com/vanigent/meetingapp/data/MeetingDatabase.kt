package com.vanigent.meetingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanigent.meetingapp.data.local.AttendeeListConverter
import com.vanigent.meetingapp.data.local.BitmapTypeConverter
import com.vanigent.meetingapp.data.local.ReceiptListConverter
import com.vanigent.meetingapp.data.local.entity.MeetingEntity

@Database(
    entities = [MeetingEntity::class],
    version = 1
)
//@TypeConverters(
//    BitmapTypeConverter::class,
//    AttendeeListConverter::class,
//    ReceiptListConverter::class
//)
@TypeConverters(Converters::class)
abstract class MeetingDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
}