package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.dto.CoordinatorDto
import com.vanigent.meetingapp.data.local.entity.CoordinatorEntity
import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.util.EncryptedData

object CoordinatorMapper {
    fun mapToEntity(coordinator: Coordinator): CoordinatorEntity {
        return CoordinatorEntity(
            username = coordinator.username,
            encryptedData = EncryptedData(password = coordinator.password),
            name = coordinator.fullName
        )
    }

    fun mapToDomain(coordinator: CoordinatorEntity): Coordinator {
        return Coordinator(
            username = coordinator.username,
            password = coordinator.encryptedData.password,
            fullName = coordinator.name
        )
    }

    fun mapEntityToDto(coordinatorEntity: CoordinatorEntity): CoordinatorDto {
        return CoordinatorDto(
            fullName = coordinatorEntity.name ?: ""
        )
    }

}