package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.ReceiptEntity

object ReceiptMapper {
    fun mapToEntity(receipt: ReceiptEntity): ReceiptEntity {
        return ReceiptEntity(
            receiptItems = receipt.receiptItems,
            receiptImagePath = receipt.receiptImagePath
        )
    }

    fun mapToDomain(receiptEntity: ReceiptEntity): ReceiptEntity {
        return ReceiptEntity(
            receiptItems = receiptEntity.receiptItems,
            receiptImagePath = receiptEntity.receiptImagePath
        )
    }
}

