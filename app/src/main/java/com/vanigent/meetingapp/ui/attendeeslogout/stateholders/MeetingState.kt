package com.vanigent.meetingapp.ui.attendeeslogout.stateholders

import com.vanigent.meetingapp.domain.model.Meeting

data class MeetingState(
    val meetings: List<Meeting> = emptyList()
)
