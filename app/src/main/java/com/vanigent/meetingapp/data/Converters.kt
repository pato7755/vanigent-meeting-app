package com.vanigent.meetingapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vanigent.meetingapp.data.local.entity.AttendeeEntity
import com.vanigent.meetingapp.data.local.entity.ReceiptEntity

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAttendeeList(attendees: List<AttendeeEntity>): String {
        return gson.toJson(attendees)
    }

    @TypeConverter
    fun toAttendeeList(attendeeString: String): List<AttendeeEntity> {
        val listType = object : TypeToken<List<AttendeeEntity>>() {}.type
        return gson.fromJson(attendeeString, listType)
    }

    @TypeConverter
    fun fromReceiptList(receipts: List<ReceiptEntity>): String {
        return gson.toJson(receipts)
    }

    @TypeConverter
    fun toReceiptList(receiptString: String?): List<ReceiptEntity> {
        if (receiptString.isNullOrEmpty()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<ReceiptEntity>>() {}.type
        return gson.fromJson(receiptString, listType) ?: emptyList()
    }
}
