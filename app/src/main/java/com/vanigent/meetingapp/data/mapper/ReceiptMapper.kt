package com.vanigent.meetingapp.data.mapper

import com.vanigent.meetingapp.data.local.entity.ReceiptEntity

object ReceiptMapper {
    fun mapToEntity(receipt: ReceiptEntity): ReceiptEntity {
        return ReceiptEntity(
            image = receipt.image,
            vendorName = receipt.vendorName,
            amount = receipt.amount,
            caterer = receipt.caterer,
            date = receipt.date
        )
    }

    fun mapToDomain(receiptEntity: ReceiptEntity): com.vanigent.meetingapp.data.local.entity.ReceiptEntity {
        return ReceiptEntity(
            image = receiptEntity.image,
            vendorName = receiptEntity.vendorName,
            amount = receiptEntity.amount,
            caterer = receiptEntity.caterer,
            date = receiptEntity.date
        )
    }
}

