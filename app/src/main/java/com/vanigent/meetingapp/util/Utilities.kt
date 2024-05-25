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
        val parts = this.split(" ")
        val firstName = parts.first()
        val lastName = parts.last()
        return Pair(firstName, lastName)
    }

}