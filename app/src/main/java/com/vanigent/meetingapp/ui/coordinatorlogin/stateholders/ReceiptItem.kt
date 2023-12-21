package com.vanigent.meetingapp.ui.coordinatorlogin.stateholders

import android.graphics.Bitmap

data class ReceiptItem(
    val title: String,
    val mapOfStrings: MutableMap<String, String>,
    val bitmap: Bitmap?
)