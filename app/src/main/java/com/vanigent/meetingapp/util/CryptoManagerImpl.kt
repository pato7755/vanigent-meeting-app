package com.vanigent.meetingapp.util

import android.security.keystore.KeyProperties
import android.util.Base64
import com.vanigent.meetingapp.domain.repository.CryptoManager
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManagerImpl : CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val key: SecretKey by lazy {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES).apply {
            init(256)
        }.generateKey()
    }

    override fun encryptWithGeneratedIV(password: String): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            // Generate a random IV for GCM mode
            val iv = ByteArray(12) // IV length for GCM mode is 12 bytes
            SecureRandom().nextBytes(iv)
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))
        }
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        val base64String = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        val authenticationTag = cipher.parameters.getParameterSpec(GCMParameterSpec::class.java).iv
        return EncryptedData(password = base64String, iv = authenticationTag)
    }

    override fun encrypt(password: String, iv: ByteArray): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            // Generate a random IV for GCM mode
//            val iv = ByteArray(12) // IV length for GCM mode is 12 bytes
//            SecureRandom().nextBytes(iv)
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))
        }
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        val base64String = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        val authenticationTag = cipher.parameters.getParameterSpec(GCMParameterSpec::class.java).iv
        return EncryptedData(password = base64String, iv = authenticationTag)
    }

    override fun decrypt(encryptedData: EncryptedData): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, encryptedData.iv))
        }
        return String(cipher.doFinal(Base64.decode(encryptedData.password, Base64.DEFAULT)))
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}

data class EncryptedData(val password: String, val iv: ByteArray? = null)