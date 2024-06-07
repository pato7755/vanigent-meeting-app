package com.vanigent.meetingapp.ui.attendeeslogin.stateholders

import androidx.compose.ui.graphics.ImageBitmap
import com.vanigent.meetingapp.ui.attendeeslogin.components.Line

data class SignatureState(
    val bitmap: ImageBitmap?,
    val signatureLines: MutableList<Line>,
    val isValid: Boolean? = null
)
