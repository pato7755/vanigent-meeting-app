package com.vanigent.meetingapp.domain.repository

import com.vanigent.meetingapp.util.EncryptedData

interface CryptoManager {

    fun encryptWithGeneratedIV(password: String): EncryptedData

    fun encrypt(password: String, iv: ByteArray): EncryptedData

    fun decrypt(encryptedData: EncryptedData): String

}