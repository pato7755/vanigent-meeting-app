package com.vanigent.meetingapp.util

import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateUtilities {

    fun getCurrentDate(): String {
        val result: String
        try {
            val currentDate = LocalDate.now()
            val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            result = currentDate.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            Timber.d("DateTimeParseException - ${e.localizedMessage}")
            return "Default"
        }
        return result
    }
}