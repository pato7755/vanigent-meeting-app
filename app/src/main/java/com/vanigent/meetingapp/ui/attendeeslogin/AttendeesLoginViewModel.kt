package com.vanigent.meetingapp.ui.attendeeslogin

import androidx.lifecycle.ViewModel
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.FirstNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.LastNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.PIDState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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


}