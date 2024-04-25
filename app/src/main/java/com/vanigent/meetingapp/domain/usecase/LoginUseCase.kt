package com.vanigent.meetingapp.domain.usecase

import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: MeetingRepository
) {

    suspend operator fun invoke(coordinator: Coordinator) =
        repository.login(coordinator = coordinator)

}