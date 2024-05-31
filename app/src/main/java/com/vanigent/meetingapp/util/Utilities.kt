package com.vanigent.meetingapp.util

import timber.log.Timber
import java.lang.NumberFormatException

object Utilities {

    fun String.removeDollar(): Double =
        try {
            this.removePrefix("$").toDouble()
        } catch (ex: NumberFormatException) {
            Timber.d("An exception occurred while converting amount : $this -> $ex")
            0.00
        }

    fun String.splitName(): Pair<String, String> {
        val parts = this.trim().split(" ")
        if (parts.size == 1) {
            // If there's only one part, assume it's a single name (edge case)
            return Pair(parts.first(), "")
        }
        val firstName = parts.dropLast(1).joinToString(" ")
        val lastName = parts.last()
        return Pair(firstName, lastName)
    }


}