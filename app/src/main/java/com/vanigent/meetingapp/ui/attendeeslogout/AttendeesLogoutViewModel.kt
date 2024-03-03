package com.vanigent.meetingapp.ui.attendeeslogout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.common.WorkResult
import com.vanigent.meetingapp.domain.usecase.FetchMeetingsUseCase
import com.vanigent.meetingapp.ui.attendeeslogout.stateholders.MeetingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendeesLogoutViewModel @Inject constructor(
    private val fetchMeetingsUseCase: FetchMeetingsUseCase
) : ViewModel() {

    private val _meetingState = MutableStateFlow(MeetingState())
    val meetingState = _meetingState.asStateFlow()

    init {
        fetchMeetings()
    }

    private fun fetchMeetings() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMeetingsUseCase.invoke().collectLatest { result ->
                when (result) {
                    is WorkResult.Loading -> {

                    }

                    is WorkResult.Success -> {
                        _meetingState.update { state ->
                            state.copy(
                                meetings = result.data ?: emptyList()
                            )
                        }

                        _meetingState.value.meetings.map {
                            println("_meetingState - ${it.attendee.map { 
                                a -> a.attendeeFirstName 
                            }}")
                        }

                    }

                    is WorkResult.Error -> {

                    }
                }

            }
        }
    }

}