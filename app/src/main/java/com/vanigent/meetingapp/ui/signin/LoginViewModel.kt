package com.vanigent.meetingapp.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.model.Coordinator
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.domain.usecase.LoginUseCase
import com.vanigent.meetingapp.ui.signin.stateholders.LoginScreenState
import com.vanigent.meetingapp.ui.signin.stateholders.PasswordState
import com.vanigent.meetingapp.ui.signin.stateholders.UsernameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MeetingRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _usernameState = MutableStateFlow(UsernameState())
    val usernameState = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(PasswordState())
    val passwordState = _passwordState.asStateFlow()

    private val _screenState = MutableStateFlow(LoginScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        repository.dbSetup()
    }

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

    private fun toggleLoadingState() {
        _screenState.update { state ->
            state.copy(loadingState = !state.loadingState)
        }
    }

    fun login() {
        val coordinator = Coordinator(
            username = _usernameState.value.username,
            password = _passwordState.value.password
        )
        _screenState.update { it.copy(loadingState = true) }

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = loginUseCase.invoke(coordinator)) {
                is WorkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        _screenState.update { state ->
                            state.copy(
                                loginResultState = true,
                                loadingState = false
                            )
                        }
                    }
                }

                is WorkResult.Error -> {

                    _screenState.update { state ->
                        state.copy(
                            loginResultState = false,
                            snackBarVisibility = true,
                            loadingState = false,
                            errorMessage = result.message ?: "An error occurred"
                        )
                    }
                }

                else -> {
                    _screenState.update { it.copy(loadingState = false) }
                }
            }
        }
    }

    fun resetSnackBarVisibility() {
        _screenState.update { state ->
            state.copy(
                snackBarVisibility = false
            )
        }
    }
}