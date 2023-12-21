package com.vanigent.meetingapp.ui.coordinatorlogin.stateholders

import android.graphics.Bitmap

data class ExtractedTextState(
    var receiptNumber: Int = 0,
    val mapOfStrings: MutableMap<String, String>,
    val bitmap: Bitmap?,
    val receiptItems: List<ReceiptItem>
)

