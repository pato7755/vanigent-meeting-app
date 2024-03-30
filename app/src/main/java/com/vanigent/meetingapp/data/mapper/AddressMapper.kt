package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.AddressEntity
import com.vanigent.meetingapp.domain.model.Address

object AddressMapper {
    fun mapToEntity(address: Address): AddressEntity {
        return AddressEntity(
            physicianName = address.physicianName,
            officeName = address.officeName,
            lineOne = address.lineOne,
            city = address.city,
            state = address.state,
            country = address.country
        )
    }

    fun mapToDomain(addressEntity: AddressEntity): Address {
        return Address(
            physicianName = addressEntity.physicianName,
            officeName = addressEntity.officeName,
            lineOne = addressEntity.lineOne,
            city = addressEntity.city,
            state = addressEntity.state,
            country = addressEntity.country
        )
    }
}