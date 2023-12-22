package com.vanigent.meetingapp.ui.coordinatorlogin

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.BitmapState
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.ExtractedTextState
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.ReceiptItem
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.SearchBarState
import com.vanigent.meetingapp.util.SampleAddresses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CoordinatorLoginViewModel @Inject constructor() : ViewModel() {

    private val _searchBarState = MutableStateFlow(SearchBarState())
    val searchBarState = _searchBarState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    private val _bitmapState = MutableStateFlow(BitmapState())
    val bitmapState = _bitmapState.asStateFlow()

    private val _extractedTextState = MutableStateFlow(
        ExtractedTextState(
            receiptNumber = 0,
            mapOfStrings = mutableStateMapOf(),
            bitmap = null,
            receiptItems = emptyList()
        )
    )
    val extractedTextState = _extractedTextState.asStateFlow()


//    fun updateBitmapHolder(bitmap: Bitmap?) {
//        bitmap?.let {
//            _bitmapState.update { state ->
//                state.copy(
//                    bitmap = it
//                )
//            }
//        }
//    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
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
            val trimmedPhysicianName = address.physicianName.trim().lowercase()
            val trimmedLineOne = address.lineOne.trim().lowercase()
            val trimmedCity = address.city.trim().lowercase()
            val trimmedState = address.state.trim().lowercase()

            searchText in trimmedPhysicianName
                    || searchText in trimmedLineOne
                    || searchText in trimmedCity
                    || searchText in trimmedState
        }.map { it.lineOne }
    }

    fun onAddressItemSelected(address: String) {
        _searchBarState.update {
            it.copy(
                searchString = address
            )
        }
    }

    fun updateReceiptDetails(extractedText: MutableMap<String, String>, bitmap: Bitmap) {
        Timber.e("call updateReceiptDetails")
        extractedText.map {
            Timber.e("extractedText VM:\n ${it.key} - ${it.value}")
        }

        _extractedTextState.update { state ->
            val newMap = state.mapOfStrings.toMutableMap().apply { putAll(extractedText) }

            val updatedReceiptItems = state.receiptItems.toMutableList().apply {
                add(
                    ReceiptItem(
                        title = "Receipt ${state.receiptNumber + 1}",
                        mapOfStrings = newMap.toMutableMap(), // Create a deep copy here
                        bitmap = bitmap
                    )
                )
            }

            state.copy(
                receiptNumber = state.receiptNumber + 1,
                mapOfStrings = newMap.toMutableMap(), // Create a deep copy here
                bitmap = bitmap,
                receiptItems = updatedReceiptItems
            )
        }
    }
}
