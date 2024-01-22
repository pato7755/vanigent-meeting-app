package com.vanigent.meetingapp.ui.attendeeslogin

import androidx.lifecycle.ViewModel
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.DialogState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.FirstNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.LastNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.PIDState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.SnackbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendeesLoginViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _selectedDropdownOption = MutableStateFlow("")
    val selectedDropdownOption = _selectedDropdownOption.asStateFlow()

    private val _firstName = MutableStateFlow(FirstNameState())
    val firstNameState = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow(LastNameState())
    val lastNameState = _lastName.asStateFlow()

    private val _pid = MutableStateFlow(PIDState())
    val pidState = _pid.asStateFlow()

    private val _dialogVisibility = MutableStateFlow(DialogState(false))
    val dialogVisibility = _dialogVisibility.asStateFlow()

    private val _snackbarVisibility = MutableStateFlow(SnackbarState(false))
    val snackbarVisibility = _snackbarVisibility.asStateFlow()


    fun onFirstNameTextChanged(text: String) {
        _firstName.update { state ->
            state.copy(
                firstName = text
            )
        }
    }

    fun onLastNameTextChanged(text: String) {
        _lastName.update { state ->
            state.copy(
                lastName = text
            )
        }
    }

    fun onPidTextChanged(text: String) {
        _pid.update { state ->
            state.copy(
                pId = text
            )
        }
    }

    fun fetchProfessionalDesignations() = meetingRepository.getProfessionalDesignations()

    fun updateDropdownSelectedOption(selectedOption: String) {
        _selectedDropdownOption.value = selectedOption
    }

    fun toggleDialogVisibility() {
        _dialogVisibility.update { state ->
            state.copy(
                isVisible = !state.isVisible
            )
        }
    }

    fun toggleSnackbarVisibility() {
        _snackbarVisibility.update { state ->
            state.copy(
                isVisible = !state.isVisible
            )
        }
        Timber.e("_snackbarVisibility.value.isVisible - ${_snackbarVisibility.value.isVisible}")
    }

    fun performFieldValidations() {

        val isFirstNameValid = isNameValid(_firstName.value.firstName)

        val isLastNameValid = isNameValid(_lastName.value.lastName)

        val isPidValid = isPidValid(_pid.value.pId)

        _firstName.update { state ->
            state.copy(isValid = isFirstNameValid || state.firstName.isNotBlank())
        }

        _lastName.update { state ->
            state.copy(isValid = isLastNameValid || state.lastName.isNotBlank())
        }

        _pid.update { state ->
            state.copy(isValid = isPidValid || state.pId.isNotBlank())
        }

        val isFormValid = isFirstNameValid && isLastNameValid && isPidValid

        if (isFormValid) {
            // Save entries and clear form
            toggleSnackbarVisibility()
        } else if (!isFirstNameValid && !isLastNameValid && !isPidValid) {
            // Ask coordinator to enter password
            toggleDialogVisibility()
        }

    }

    private fun isNameValid(text: String): Boolean {
        return text.matches(Regex("^[A-Za-z]+\$"))
    }

    private fun isPidValid(number: String): Boolean {
        return number.matches(Regex("^\\d+\$"))
    }

    private fun isFormBlank(): Boolean {
        return _firstName.value.firstName.isBlank() && _lastName.value.lastName.isBlank() && _pid.value.pId.isBlank()
    }


}