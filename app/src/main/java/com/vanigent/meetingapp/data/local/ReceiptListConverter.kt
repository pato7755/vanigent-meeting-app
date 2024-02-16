package com.vanigent.meetingapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vanigent.meetingapp.data.local.entity.ReceiptEntity

object ReceiptListConverter {
    @TypeConverter
    fun fromReceiptList(receiptList: List<ReceiptEntity>): String {
        val gson = Gson()
        return gson.toJson(receiptList)
    }

    @TypeConverter
    fun toReceiptList(receiptListString: String): List<ReceiptEntity> {
        val listType = object : TypeToken<List<ReceiptEntity>>() {}.type
        return Gson().fromJson(receiptListString, listType)
    }
}
