package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.data.CoordinatorDao
import com.vanigent.meetingapp.data.mapper.CoordinatorMapper
import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.domain.repository.CoordinatorRepository
import javax.inject.Inject

class CoordinatorRepositoryImpl @Inject constructor(
    private val coordinatorDao: CoordinatorDao
) : CoordinatorRepository {
    override suspend fun fetchCoordinatorDetails(): Coordinator {
        return CoordinatorMapper.mapToDomain(coordinatorDao.fetchCoordinatorDetails())
    }
}