package com.vanigent.meetingapp.ui.coordinatorlogin

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import com.vanigent.meetingapp.util.SampleAddresses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CoordinatorLoginViewModel @Inject constructor() : ViewModel() {

    private val _searchBarState = MutableStateFlow(SearchBarState())
    val searchBarState = _searchBarState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    private val _recognizedText = MutableStateFlow(RecognizedText("Receipt 1", mutableStateMapOf()))
    val recognizedText = _recognizedText.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

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

    fun updateReceiptDetails(extractedText: MutableMap<String, String>) {
        Timber.d("extractedText VM - $extractedText")
        _recognizedText.update { state ->
            state.copy(
                mapOfStrings = extractedText
            )
        }
    }

}
