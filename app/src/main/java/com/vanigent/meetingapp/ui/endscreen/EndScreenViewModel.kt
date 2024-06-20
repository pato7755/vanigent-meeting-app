package com.vanigent.meetingapp.ui.endscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndScreenViewModel @Inject constructor(

) : ViewModel() {

    private val _isFinished = MutableStateFlow(false)
    val isFinished = _isFinished.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isFinished.value = true
        }
    }
}