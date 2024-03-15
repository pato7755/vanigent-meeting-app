package com.vanigent.meetingapp.ui.coordinatorlogin

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanigent.meetingapp.domain.model.Address
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.domain.model.Receipt
import com.vanigent.meetingapp.domain.model.SampleAddresses
import com.vanigent.meetingapp.domain.usecase.SaveMeetingUseCase
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.BitmapState
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.ExtractedTextState
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.ReceiptItem
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.SearchBarState
import com.vanigent.meetingapp.util.FileUtilities.storeBitmapImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CoordinatorLoginViewModel @Inject constructor(
    private val saveMeetingUseCase: SaveMeetingUseCase,
    private val context: Context
) : ViewModel() {

    private val _searchBarState = MutableStateFlow(SearchBarState())
    val searchBarState = _searchBarState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    private val _bitmapState = MutableStateFlow(BitmapState())
    val bitmapState = _bitmapState.asStateFlow()

    private val radioButtonSelection = MutableStateFlow(true)

    private val _extractedTextState = MutableStateFlow(
        ExtractedTextState(
            receiptNumber = 0,
            mapOfStrings = mutableStateMapOf(),
            bitmap = null,
            receiptItems = emptyList()
        )
    )
    val extractedTextState = _extractedTextState.asStateFlow()

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

    fun onSearchAddress(text: String): List<Address> {
        val searchText = text.trim().lowercase()

        return SampleAddresses.addresses.filter { address ->
            val officeName = address.officeName.trim().lowercase()
            val trimmedPhysicianName = address.physicianName.trim().lowercase()
            val trimmedLineOne = address.lineOne.trim().lowercase()
            val trimmedCity = address.city.trim().lowercase()
            val trimmedState = address.state.trim().lowercase()

            searchText in officeName
                    || searchText in trimmedPhysicianName
                    || searchText in trimmedLineOne
                    || searchText in trimmedCity
                    || searchText in trimmedState
        }
    }

    fun onAddressItemSelected(address: Address) {
        _searchBarState.update {
            it.copy(
                searchString = address.officeName,
                selectedResultsDisplay = address
            )
        }
    }

    fun radioButtonSelection(selection: Boolean) {
        radioButtonSelection.value = selection
    }

    fun updateReceiptDetails(extractedText: MutableMap<String, String>, bitmap: Bitmap) {

        _extractedTextState.update { state ->
            val newMap = state.mapOfStrings.toMutableMap().apply { putAll(extractedText) }

            val updatedReceiptItems = state.receiptItems.toMutableList().apply {
                add(
                    ReceiptItem(
                        title = "Receipt ${state.receiptNumber + 1}",
                        mapOfStrings = newMap.toMutableMap(),
                        bitmap = bitmap
                    )
                )
            }

            state.copy(
                receiptNumber = state.receiptNumber + 1,
                mapOfStrings = newMap.toMutableMap(),
                bitmap = bitmap,
                receiptItems = updatedReceiptItems
            )
        }
    }

    fun removeReceiptImage() {
        // Remove the last item from mapOfStrings
        _extractedTextState.value.mapOfStrings.remove(_extractedTextState.value.mapOfStrings.keys.last())

        // Subtract 1 from the receipt number
        val updatedReceiptNumber = _extractedTextState.value.receiptNumber - 1

        // Remove the last item from receiptItems
        val updatedReceiptItems = _extractedTextState.value.receiptItems.dropLast(1)

        // Update the ExtractedTextState with the modified values
        _extractedTextState.value = _extractedTextState.value.copy(
            receiptNumber = updatedReceiptNumber,
            mapOfStrings = _extractedTextState.value.mapOfStrings.toMutableMap(),
            receiptItems = updatedReceiptItems
        )
    }

    fun saveMeetingDetails(onMeetingIdReceived: (String) -> Unit) {

        val officeName = _searchBarState.value.searchString
        viewModelScope.launch(Dispatchers.IO) {
            val meetingId = saveMeetingUseCase.invoke(
                meeting = Meeting(
                    officeLocation = officeName ?: "",
                    coordinatorWillConsumeFood = radioButtonSelection.value,
                    receipt = extractedTextStateToReceipts(_extractedTextState.value),
                    attendee = listOf()
                )
            )
            withContext(Dispatchers.Main) {
                onMeetingIdReceived(meetingId.toString())
            }
        }
    }

    // Function to map ReceiptItem to Receipt
    private fun mapReceiptItemToReceipt(receiptItem: ReceiptItem): Receipt {
        val filePath = receiptItem.bitmap?.let {
            storeBitmapImage(
                context = context,
                fileName = receiptItem.title.plus(".jpg"),
                imageBitmap = it
            )
        }
        Timber.d("filePath - $filePath")
        return Receipt(
            receiptItems = receiptItem.mapOfStrings,
            receiptImagePath = if (filePath.isNullOrBlank()) "" else filePath
        )
    }

    // Function to convert ExtractedTextState to List<Receipt>
    private fun extractedTextStateToReceipts(extractedTextState: ExtractedTextState): List<Receipt> {
        return extractedTextState.receiptItems.map { mapReceiptItemToReceipt(it) }
    }

}


