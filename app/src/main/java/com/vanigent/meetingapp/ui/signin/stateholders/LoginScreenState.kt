package com.vanigent.meetingapp.ui.signin.stateholders

data class LoginScreenState(
    val loginState: Boolean = false,
    val loadingState: Boolean = false,
    var snackBarVisibility: Boolean = false,
    val errorMessage: String = ""
)
