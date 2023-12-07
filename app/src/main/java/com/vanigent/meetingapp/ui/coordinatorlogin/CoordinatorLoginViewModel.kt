package com.vanigent.meetingapp.ui.coordinatorlogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.util.SampleAddresses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoordinatorLoginViewModel @Inject constructor(
//    private val permissionHandlerUseCase: PermissionHandlerUseCase
) : ViewModel() {

    private val _searchBarState = MutableStateFlow(SearchBarState())
    val searchBarState = _searchBarState.asStateFlow()

    init {
        viewModelScope.launch {
//            if (!permissionHandlerUseCase.isCameraPermissionGranted()) {
//                permissionHandlerUseCase.requestCameraPermission()
//            }
        }

    }

    fun onSearchTextChanged(text: String) {
        _searchBarState.update { state ->
            state.copy(
                searchString = text,
            )
        }
    }


    fun onSearchAddress(text: String): List<String> {

        val searchText = text.trim().lowercase()

        return SampleAddresses.addresses.filter { address ->
            val trimmedLineOne = address.lineOne.trim().lowercase()
            val trimmedCity = address.city.trim().lowercase()
            val trimmedState = address.state.trim().lowercase()

            searchText in trimmedLineOne || searchText in trimmedCity || searchText in trimmedState
        }.map { it.lineOne }
    }

    fun onAddressItemSelected(address: String) {
        _searchBarState.update {
            it.copy(
                searchString = address
            )
        }
    }


}