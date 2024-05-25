package com.vanigent.meetingapp.domain.repository

import com.vanigent.meetingapp.domain.model.Coordinator

interface CoordinatorRepository {

    suspend fun fetchCoordinatorDetails(): Coordinator

}
