package com.vanigent.meetingapp.data.repository

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.content.Context
import com.vanigent.meetingapp.domain.repository.PermissionHandlerRepository

class PermissionHandlerRepositoryImpl(private val context: Context) : PermissionHandlerRepository {
    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST = 34

    override suspend fun isCameraPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context, CAMERA_PERMISSION
        )
    }

    override suspend fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(CAMERA_PERMISSION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
        )
    }
}