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


}