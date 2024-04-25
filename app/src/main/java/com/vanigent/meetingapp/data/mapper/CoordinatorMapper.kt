package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.CoordinatorEntity
import com.vanigent.meetingapp.domain.model.Coordinator

object CoordinatorMapper {
    fun mapToEntity(coordinator: Coordinator): CoordinatorEntity {
        return CoordinatorEntity(
            username = coordinator.username,
            password = coordinator.password,
            name = coordinator.fullName
        )
    }

    fun mapToDomain(coordinator: CoordinatorEntity): Coordinator {
        return Coordinator(
            username = coordinator.username,
            password = coordinator.password,
            fullName = coordinator.name
        )
    }

}