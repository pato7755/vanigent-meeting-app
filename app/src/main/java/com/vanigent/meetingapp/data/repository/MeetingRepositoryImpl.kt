package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.domain.repository.MeetingRepository

class MeetingRepositoryImpl: MeetingRepository {

    override fun getProfessionalDesignations(): List<String> {
        return listOf(
            "MD",
            "DO",
            "PA",
            "NP",
            "PharmD",
            "RPh",
            "RN",
            "LPN",
            "MA",
            "Office Staff"
        )
    }
}