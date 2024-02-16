package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.ReceiptEntity

object ReceiptMapper {
    fun mapToEntity(receipt: ReceiptEntity): ReceiptEntity {
        return ReceiptEntity(
            receiptItems = receipt.receiptItems,
            receiptImage = receipt.receiptImage
        )
    }

    fun mapToDomain(receiptEntity: ReceiptEntity): com.vanigent.meetingapp.data.local.entity.ReceiptEntity {
        return ReceiptEntity(
            receiptItems = receiptEntity.receiptItems,
            receiptImage = receiptEntity.receiptImage
        )
    }
}

