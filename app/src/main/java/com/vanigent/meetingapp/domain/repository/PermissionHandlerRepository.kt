package com.vanigent.meetingapp.domain.repository

interface PermissionHandlerRepository {
    suspend fun isCameraPermissionGranted(): Boolean
    suspend fun requestCameraPermission()
}