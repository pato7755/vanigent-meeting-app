package com.vanigent.meetingapp.ui.coordinatorlogin

data class ExtractedTextState(
    var receiptNumber: Int = 0,
    val mapOfStrings: MutableMap<String, String>
)