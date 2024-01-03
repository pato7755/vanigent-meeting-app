package com.vanigent.meetingapp.ui.signin

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.vanigent.meetingapp.ui.signin.stateholders.PasswordState
import com.vanigent.meetingapp.ui.signin.stateholders.UsernameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _usernameState = MutableStateFlow(UsernameState())
    val usernameState = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(PasswordState())
    val passwordState = _passwordState.asStateFlow()

    fun onUsernameTextChanged(text: String) {
        _usernameState.update { state ->
            state.copy(
                username = text
            )
        }
    }

    fun onPasswordTextChanged(text: String) {
        _passwordState.update { state ->
            state.copy(
                password = text
            )
        }
    }


}