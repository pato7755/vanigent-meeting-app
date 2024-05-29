package com.vanigent.meetingapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanigent.meetingapp.ui.MainScreen
import com.vanigent.meetingapp.ui.common.CameraPermissionTextProvider
import com.vanigent.meetingapp.ui.common.PermissionDialog
import com.vanigent.meetingapp.ui.common.ReadStoragePermissionTextProvider
import com.vanigent.meetingapp.ui.common.WriteStoragePermissionTextProvider
import com.vanigent.meetingapp.ui.coordinatorlogin.CoordinatorLoginViewModel
import com.vanigent.meetingapp.ui.signin.LoginViewModel
import com.vanigent.meetingapp.ui.theme.MeetingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            writeStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            readStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        loginViewModel.setCameraPermissionResult(isGranted)
    }

    private val writeStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        loginViewModel.setWriteStoragePermissionResult(isGranted)
    }

    private val readStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        loginViewModel.setReadStoragePermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

//                    val permissionsGranted = checkPermissions()
//                    loginViewModel.setPermissionsResult(permissionsGranted)

                        requestPermissions()

                    MainScreen()
                }

            }
        }
    }

//    private fun checkPermissions(): Boolean {
//        val cameraPermission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.CAMERA
//        )
//        val writeStoragePermission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        val readStoragePermission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
//                writeStoragePermission == PackageManager.PERMISSION_GRANTED &&
//                readStoragePermission == PackageManager.PERMISSION_GRANTED
//    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MeetingAppTheme {
    }
}