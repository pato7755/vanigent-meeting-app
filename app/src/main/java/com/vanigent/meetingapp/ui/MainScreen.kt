package com.vanigent.meetingapp.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanigent.meetingapp.ui.attendeeslogin.AttendeesLoginScreen
import com.vanigent.meetingapp.ui.attendeeslogout.AttendeesLogoutScreen
import com.vanigent.meetingapp.ui.coordinatorlogin.CoordinatorLoginScreen
import com.vanigent.meetingapp.ui.endscreen.EndScreen
import com.vanigent.meetingapp.ui.signin.LoginScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            LoginScreen(
                onLoginButtonClicked = {
                    navController.navigate(route = "coordinator_login")
                }
            )
        }

        composable(route = "coordinator_login") {
            CoordinatorLoginScreen(
                onContinueButtonPressed = { meetingId ->
                    navController.navigate(route = "attendees_login/$meetingId")
                }
            )
        }

        composable(route = "attendees_login/{meetingId}") {
            AttendeesLoginScreen(
                onContinueButtonPressed = { meetingId ->
                    navController.navigate(route = "attendees_logout/$meetingId")
                }
            )
        }

        composable(route = "attendees_logout/{meetingId}") {
            AttendeesLogoutScreen(
                onUploadSuccess = {
                    navController.navigate(route = "end_screen")
                }
            )
        }

        composable(route = "end_screen") {
            EndScreen()
        }

    }

}