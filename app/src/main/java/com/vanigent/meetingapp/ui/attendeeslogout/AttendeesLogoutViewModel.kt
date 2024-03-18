package com.vanigent.meetingapp.ui.attendeeslogout

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.usecase.FetchMeetingsUseCase
import com.vanigent.meetingapp.ui.attendeeslogout.stateholders.MeetingState
import com.vanigent.meetingapp.util.FileUtilities.generatePDF
import com.vanigent.meetingapp.util.Utilities.removeDollar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendeesLogoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchMeetingsUseCase: FetchMeetingsUseCase,
    private val context: Context
) : ViewModel() {

    private val meetingId = MutableStateFlow("")

    private val _meetingState = MutableStateFlow(MeetingState(null))
    val meetingState = _meetingState.asStateFlow()

    private val _isCostPerAttendeeValidated = MutableStateFlow(false)
    val isCostPerAttendeeValidated = _isCostPerAttendeeValidated.asStateFlow()

    private val _numberOfAttendees = MutableStateFlow(0)
    val numberOfAttendees = _numberOfAttendees.asStateFlow()

    private val _numberOfThoseWhoAte = MutableStateFlow(0)
    val numberOfThoseWhoAte = _numberOfThoseWhoAte.asStateFlow()

    private val _costOfMeal = MutableStateFlow(0.00)
    val costOfMeal = _costOfMeal.asStateFlow()

    private val _averageCostOfMeal = MutableStateFlow(0.00)
    val averageCostOfMeal = _averageCostOfMeal.asStateFlow()

    init {
        meetingId.value = savedStateHandle.get<String>("meetingId") ?: ""
        Timber.d("meetingId.value - ${meetingId.value}")
        fetchMeeting(meetingId.value.toLong())
    }

    private fun fetchMeeting(meetingId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMeetingsUseCase.invoke(meetingId).collectLatest { result ->
                when (result) {
                    is WorkResult.Loading -> {

                    }

                    is WorkResult.Success -> {
                        _meetingState.update { state ->
                            state.copy(
                                meeting = result.data
                            )
                        }

                        _numberOfAttendees.update {
                            _meetingState.value.meeting?.attendee?.count() ?: 0
                        }

                        _numberOfThoseWhoAte.update {
                            _meetingState.value.meeting?.attendee?.count { x -> x.attendeeWillConsumeFood }
                                ?: 0
                        }

                        _costOfMeal.update {
                            _meetingState.value.meeting?.receipt
                                ?.flatMap { item -> item.receiptItems.entries }
                                ?.filter { (key, _) -> key == "TOTAL" }
                                ?.sumOf { (_, value) -> value.removeDollar() } ?: 0.00
                        }

                        _averageCostOfMeal.update {
                            _costOfMeal.value.let { total ->
                                if (total > 0.00) total.div(_numberOfAttendees.value) else 0.00
                            }
                        }

                    }

                    is WorkResult.Error -> {

                    }
                }

            }
        }
    }

    fun onContinuePressed() {
        val filename = "Default.pdf"
        val meetingStatistics = mapOf(
            "Number of attendees" to _numberOfAttendees.value.toString(),
            "Number of those who consumed food" to _numberOfThoseWhoAte.value.toString(),
            "Total cost of meal" to _costOfMeal.value.toString(),
            "Average cost of meal per attendee" to _averageCostOfMeal.value.toString(),
        )
        _meetingState.value.meeting?.let {
            generatePDF(
                context = context,
                filename = filename,
                meeting = it,
                meetingStatistics = meetingStatistics
            )
        }

    }

}