package com.vanigent.meetingapp.domain.usecase

import com.vanigent.meetingapp.domain.repository.PermissionHandlerRepository

class PermissionHandlerUseCase(private val permissionRepository: PermissionHandlerRepository) {
    suspend fun isCameraPermissionGranted(): Boolean {
        return permissionRepository.isCameraPermissionGranted()
    }

    suspend fun requestCameraPermission() {
        permissionRepository.requestCameraPermission()
    }
}