package com.vanigent.meetingapp.ui.attendeeslogout

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.usecase.FetchMeetingsUseCase
import com.vanigent.meetingapp.ui.attendeeslogout.stateholders.CommentState
import com.vanigent.meetingapp.ui.attendeeslogout.stateholders.MeetingState
import com.vanigent.meetingapp.util.Constants.MAXIMUM_ALLOWED_COST_PER_MEAL
import com.vanigent.meetingapp.util.DateUtilities.getCurrentDate
import com.vanigent.meetingapp.util.FileUtilities.generatePDF
import com.vanigent.meetingapp.util.Utilities.removeDollar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
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

    private val _isCostPerAttendeeWithinLimit = MutableStateFlow(false)
    val isCostPerAttendeeWithinLimit = _isCostPerAttendeeWithinLimit.asStateFlow()

    private val _numberOfAttendees = MutableStateFlow(0)
    val numberOfAttendees = _numberOfAttendees.asStateFlow()

    private val _numberOfThoseWhoAte = MutableStateFlow(0)
    val numberOfThoseWhoAte = _numberOfThoseWhoAte.asStateFlow()

    private val _costOfMeal = MutableStateFlow(0.00)
    val costOfMeal = _costOfMeal.asStateFlow()

    private val _averageCostOfMeal = MutableStateFlow(0.00)
    val averageCostOfMeal = _averageCostOfMeal.asStateFlow()

    private val _comments = MutableStateFlow(CommentState(""))
    val commentsState = _comments.asStateFlow()

    init {
        meetingId.value = savedStateHandle.get<String>("meetingId") ?: ""
        Timber.d("meetingId.value - ${meetingId.value}")
        fetchMeeting(meetingId.value.toLong())
    }

    fun onCommentsChanged(text: String) {
        val isCommentValid = text.isNotBlank()
        _comments.update { state ->
            state.copy(
                comment = text,
                isValid = isCommentValid
            )
        }
    }

    private fun validateCostPerAttendee(averageCostPerMeal: Double) {
        val isValid = averageCostPerMeal.compareTo(MAXIMUM_ALLOWED_COST_PER_MEAL) <= 0
        _isCostPerAttendeeWithinLimit.value = isValid
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

                        _numberOfAttendees.update { (_meetingState.value.meeting?.attendee?.count() ?: 0) + 1 }

                        val coordinatorWillConsumeFood = _meetingState.value.meeting?.coordinatorWillConsumeFood
                            ?: false
                        val countOfAttendeesWhoAte = _meetingState.value.meeting?.attendee?.count { it.attendeeWillConsumeFood } ?: 0

                        _numberOfThoseWhoAte.update {
                            countOfAttendeesWhoAte + if (coordinatorWillConsumeFood) 1 else 0
                        }

                        _costOfMeal.update {
                            _meetingState.value.meeting?.receipt
                                ?.flatMap { item -> item.receiptItems.entries }
                                ?.filter { (key, _) -> key == "TOTAL" }
                                ?.sumOf { (_, value) -> value.removeDollar() } ?: 0.00
                        }

                        _averageCostOfMeal.update {
                            _costOfMeal.value.let { total ->
                                if (total > 0.00) total.div(_numberOfThoseWhoAte.value) else 0.00
                            }
                        }

//                        withContext(Dispatchers.Main) {
                            validateCostPerAttendee(_averageCostOfMeal.value)
//                        }

                    }

                    is WorkResult.Error -> {

                    }
                }

            }
        }
    }

    fun onContinuePressed() {
        val filename = "Meeting " + meetingId.value + "(${getCurrentDate()}).pdf"
        val meetingStatistics = mapOf(
            "Number of attendees" to _numberOfAttendees.value.toString(),
            "Number of those who consumed food" to _numberOfThoseWhoAte.value.toString(),
            "Total cost of meal" to "$".plus(_costOfMeal.value.toString()),
            "Average cost of meal per attendee" to "$".plus(_averageCostOfMeal.value.toString()),
        )
        _meetingState.value.meeting?.let {
            generatePDF(
                context = context,
                filename = filename,
                meeting = it,
                comments = commentsState.value.comment,
                meetingStatistics = meetingStatistics
            )
        }

    }

}