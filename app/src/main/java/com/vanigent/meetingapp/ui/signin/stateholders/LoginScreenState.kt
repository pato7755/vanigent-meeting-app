package com.vanigent.meetingapp.ui.signin.stateholders

data class LoginScreenState(
    val loginResultState: Boolean = false,
    val loadingState: Boolean = false,
    var snackBarVisibility: Boolean = false,
    val errorMessage: String = ""
)
