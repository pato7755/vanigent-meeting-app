package com.vanigent.meetingapp.ui.coordinatorlogin.stateholders

import com.vanigent.meetingapp.util.Address

data class SearchBarState(
    val isLoading: Boolean = false,
    val searchString: String? = "",
    val searchResults: List<Address> = emptyList()
)