package com.vanigent.meetingapp.ui.attendeeslogin

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.domain.usecase.SaveAttendeeUseCase
import com.vanigent.meetingapp.ui.attendeeslogin.components.Line
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.DialogPasswordState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.DialogState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.ErrorState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.FirstNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.LastNameState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.PIDState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.SignatureState
import com.vanigent.meetingapp.ui.attendeeslogin.stateholders.SnackbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class AttendeesLoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val meetingRepository: MeetingRepository,
    private val saveAttendeeUseCase: SaveAttendeeUseCase
) : ViewModel() {

    private val meetingId = MutableStateFlow("")

    private val _selectedDropdownOption = MutableStateFlow("")
    val selectedDropdownOption = _selectedDropdownOption.asStateFlow()

    private val _firstName = MutableStateFlow(FirstNameState())
    val firstNameState = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow(LastNameState())
    val lastNameState = _lastName.asStateFlow()

    private val _pid = MutableStateFlow(PIDState())
    val pidState = _pid.asStateFlow()

    private val _radioButtonSelection = MutableStateFlow<Boolean?>(null)
    val radioButtonSelection = _radioButtonSelection.asStateFlow()

    private val _dialogVisibility = MutableStateFlow(DialogState(false))
    val dialogVisibility = _dialogVisibility.asStateFlow()

    private val _snackbarVisibility = MutableStateFlow(SnackbarState(false))
    val snackbarVisibility = _snackbarVisibility.asStateFlow()

    private val _isFormBlankState = MutableStateFlow(true)
    val isFormBlankState = _isFormBlankState

    private val _dialogPassword = MutableStateFlow(DialogPasswordState())
    val dialogPassword = _dialogPassword.asStateFlow()

    private val _signatureLines = mutableStateListOf<Line>()
    val signatureLines = _signatureLines

    private val _signatureBitmap = MutableStateFlow(SignatureState(null, mutableListOf()))
    val signatureBitmap = _signatureBitmap.asStateFlow()

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState = _errorState.asStateFlow()

    init {
        meetingId.value = savedStateHandle.get<String>("meetingId") ?: ""
    }

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
    }

    fun onPasswordTextChanged(text: String) {
        _dialogPassword.update { state ->
            state.copy(
                password = text
            )
        }
    }

//    fun radioButtonSelection(selection: Boolean) {
//        radioButtonSelection.value = selection
//    }

    private fun updateIsFormBlank(isFormBlank: Boolean) {
        _isFormBlankState.update {
            isFormBlank
        }
    }

    fun performFieldValidations(outputErrors: (ErrorState) -> Unit) {

        val isFirstNameValid = isNameValid(_firstName.value.firstName)

        val isLastNameValid = isNameValid(_lastName.value.lastName)

//        val isPidValid = isPidValid(_pid.value.pId)

        val isProfessionalDesignationValid = isProfessionalDesignationValid()

        val isSignatureValid = isSignatureValid()

        _errorState.update { state ->
            state.copy(
                isProfessionalDesignationValid = isProfessionalDesignationValid,
                isSignatureValid = isSignatureValid
            )
        }

        _firstName.update { state ->
            state.copy(isValid = isFirstNameValid || state.firstName.isNotBlank())
        }

        _lastName.update { state ->
            state.copy(isValid = isLastNameValid || state.lastName.isNotBlank())
        }

        outputErrors(_errorState.value)

        val isFormValid = isFirstNameValid
                && isLastNameValid
                && isProfessionalDesignationValid
                && isSignatureValid

        /**
         * if isFormValid is true, it means all fields have been filled in properly with the right data formats.
         * Set _isFormBlankState to true, show snackbar to welcome attendee, save data and clear form
         * if isFormBlank is false, either one or more entries haven't been made or have the wrong format
         *
         * if isFormBlank is true, it means a number of attendees have already signed in.
         * We then set _isFormBlankState to true, show passwordDialog, and navigate if password is correct.
         *
         * if isFormValid and isFormBlank are false, set _isFormBlankState to false so that isError for the text
         * fields takes effect.
         */
        when {
            isFormValid -> {
                updateIsFormBlank(false)
                saveAttendeeDetails()
                toggleSnackbarVisibility()
            }

            isFormBlank() -> {
                updateIsFormBlank(true)
                toggleDialogVisibility()
            }

            else -> updateIsFormBlank(false)
        }

    }

    private fun isNameValid(text: String): Boolean {
        return text.matches(Regex("^[A-Za-z -]+\$"))
    }

    private fun isFormBlank(): Boolean {
        return _firstName.value.firstName.isBlank() && _lastName.value.lastName.isBlank() && _pid.value.pId.isBlank()
    }

    private fun isProfessionalDesignationValid(): Boolean {
        return _selectedDropdownOption.value.isNotBlank()
    }

    private fun isSignatureValid(): Boolean {
        return _signatureLines.isNotEmpty()
    }

    fun updateSignature(bitmap: ImageBitmap) {
        _signatureBitmap.update { state ->
            state.copy(
                bitmap = bitmap
            )
        }
    }

    fun updateSignatureLines(newLines: MutableList<Line>) {
        _signatureLines.addAll(newLines)
    }

//    fun resetRadioButtonSelection() {
//        _radioButtonSelection.value = null
//    }

    fun setRadioButtonSelection(selection: Boolean?) {
        _radioButtonSelection.value = selection
    }

    private fun clearCanvas() {
        _signatureBitmap.value = SignatureState(bitmap = null, mutableListOf())
        _signatureLines.clear()
    }

    fun clearForm() {
        updateDropdownSelectedOption("")
        clearCanvas()
        _firstName.value = FirstNameState()
        _lastName.value = LastNameState()
        _pid.value = PIDState()
        setRadioButtonSelection(null)
        _errorState.value = ErrorState()
        updateIsFormBlank(true)
    }

    private fun saveAttendeeDetails() {
        val firstName = _firstName.value.firstName.trim()
        val lastName = _lastName.value.lastName.trim()
        val pId = _pid.value.pId.trim()
        val professionalDesignation = _selectedDropdownOption.value
        val signatureBitmap = _signatureBitmap.value.bitmap?.asAndroidBitmap()

        val signatureByteArray = signatureBitmap?.let {
            ByteArrayOutputStream().apply {
                it.compress(Bitmap.CompressFormat.PNG, 100, this)
            }.toByteArray()
        }

        viewModelScope.launch(Dispatchers.IO) {
            saveAttendeeUseCase.invoke(
                meetingId = meetingId.value.toLong(),
                attendee = Attendee(
                    attendeeFirstName = firstName,
                    attendeePid = pId,
                    attendeeLastName = lastName,
                    attendeeWillConsumeFood = radioButtonSelection.value ?: false,
                    attendeeProfessionalDesignation = professionalDesignation,
                    attendeeSignature = signatureByteArray ?: byteArrayOf()
                )
            )
        }
    }

    fun getMeetingId(onMeetingIdReceived: (String) -> Unit) {
        onMeetingIdReceived(meetingId.value)
    }
}