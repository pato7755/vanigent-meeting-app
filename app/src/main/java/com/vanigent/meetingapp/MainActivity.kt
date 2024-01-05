package com.vanigent.meetingapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanigent.meetingapp.ui.MainScreen
import com.vanigent.meetingapp.ui.common.CameraPermissionTextProvider
import com.vanigent.meetingapp.ui.common.PermissionDialog
import com.vanigent.meetingapp.ui.coordinatorlogin.CoordinatorLoginViewModel
import com.vanigent.meetingapp.ui.theme.MeetingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainScreen()

                    RequestForPermissions()
                }

            }
        }
    }

    @Composable
    private fun RequestForPermissions() {
        val viewModel = viewModel<CoordinatorLoginViewModel>()
        val dialogQueue = viewModel.visiblePermissionDialogQueue

        val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                viewModel.onPermissionResult(
                    permission = Manifest.permission.CAMERA,
                    isGranted = isGranted
                )
            }
        )

        DisposableEffect(Unit) {
            cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
            onDispose { /* cleanup, if necessary */ }
        }

        dialogQueue
            .reversed()
            .forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.CAMERA -> {
                            CameraPermissionTextProvider(this)
                        }

                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        permission
                    ),
                    onDismiss = viewModel::dismissDialog,
                    onOkClick = {
                        viewModel.dismissDialog()
                    },
                    onGoToAppSettingsClick = ::openAppSettings
                )
            }
    }
}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MeetingAppTheme {
    }
}