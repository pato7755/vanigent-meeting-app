package com.vanigent.meetingapp.ui.signin.stateholders

data class PermissionState(
    val cameraGranted: Boolean = false,
    val writeStorageGranted: Boolean = false,
    val readStorageGranted: Boolean = false
)
