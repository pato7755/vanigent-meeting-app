@file:OptIn(ExperimentalMaterial3Api::class)

package com.vanigent.meetingapp.ui.coordinatorlogin

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.common.SectionHeader
import com.vanigent.meetingapp.ui.coordinatorlogin.stateholders.ReceiptItem
import com.vanigent.meetingapp.ui.settings.ToggleableInfo
import com.vanigent.meetingapp.util.Constants.SEVENTY_PERCENT
import timber.log.Timber

@Composable
fun CoordinatorLoginScreen(
    viewModel: CoordinatorLoginViewModel = hiltViewModel()
) {
    val extractedTextState by viewModel.recognizedText.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    DataEntryForm()

                    extractedTextState.receiptItems.forEachIndexed { index, receiptItem ->
                        ReceiptDetailsCard(
                            receiptItem = receiptItem,
                        )
                        if (index < extractedTextState.receiptItems.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(SEVENTY_PERCENT)
                .fillMaxWidth()
        ) {
            ReceiptImageSection(
                extractedText = extractedTextState.mapOfStrings,
                onReceiptDetailsUpdated = viewModel::updateReceiptDetails
            )
        }
    }
}


@Composable
fun ReceiptDetailsCard(
    receiptItem: ReceiptItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            contentColor = colorResource(id = R.color.vanigent_light_green),
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        SectionHeader(
            title = receiptItem.title
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                receiptItem.bitmap?.asImageBitmap()?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(16.dp)
                    )
                }
            }
        }
        receiptItem.mapOfStrings.forEach { (key, value) ->
            println("Key: $key, Value: $value")
            ReceiptDetails(
                label = key,
                text = value
            )
        }
    }
}

@Composable
fun SearchResults(
    addresses: List<String>,
    onItemSelected: (String) -> Unit
) {
    // Display the search results here
    LazyColumn {
        items(addresses) { address ->
            Row(
                modifier = Modifier.clickable {
                    onItemSelected(address)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = stringResource(R.string.location),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = address,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: CoordinatorLoginViewModel = hiltViewModel()
) {

    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()

    // if the search bar is active or not
    var isActive by remember {
        mutableStateOf(false)
    }

    val historyItems = remember {
        mutableStateListOf<String>()
    }

    var searchResults by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isActive) 0.dp else 8.dp),
        query = searchBarState.searchString ?: "",
        onQueryChange = viewModel::onSearchTextChanged,
        onSearch = {
            searchBarState.searchString?.let { searchResults = viewModel.onSearchAddress(it) }
        },
        active = isActive,
        onActiveChange = { activeChange ->
            isActive = activeChange
        },
        placeholder = {
            Text(text = stringResource(id = R.string.office_location))
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    ) {
        historyItems.forEach { historyItem ->
            Row(modifier = Modifier.padding(all = 16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 12.dp),
                    imageVector = Icons.Default.History, contentDescription = null
                )
                Text(text = historyItem)
            }
        }
        if (searchResults.isNotEmpty())
            SearchResults(addresses = searchResults) { selectedAddress ->
                viewModel.onAddressItemSelected(selectedAddress)
                historyItems.add(selectedAddress)
                isActive = false
                searchResults = emptyList()
            }
    }

}

@Composable
fun DataEntryForm(
) {
    SearchBar()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            contentColor = colorResource(id = R.color.vanigent_light_green),
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(id = R.string.would_you_like_to_have_something_to_eat),
                style = MaterialTheme.typography.bodyLarge
            )

            RadioButtons()
        }
    }

}

@Composable
fun ReceiptImageSection(
    extractedText: MutableMap<String, String>,
    onReceiptDetailsUpdated: (MutableMap<String, String>, Bitmap) -> Unit
) {
    var showCameraPreview by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                showCameraPreview = !showCameraPreview
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        SectionHeader(stringResource(id = R.string.capture_receipts))

        Spacer(modifier = Modifier.height(8.dp))

        if (showCameraPreview) {
            Timber.d("extractedText ImageSection - $extractedText")

            CameraStuff(
                extractedText = extractedText,
                closeCameraPreview = { _ ->
                    showCameraPreview = false
                },
                onReceiptDetailsUpdated = onReceiptDetailsUpdated
            )
        }

        ReceiptImageItem()

    }

}


@Composable
fun ReceiptImageItem() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .align(Alignment.Center)
                    .background(
                        color = colorResource(R.color.vanigent_light_green),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )

            }
            Text(
                text = stringResource(id = R.string.add_photo),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}


@Composable
fun ReceiptDetails(
    label: String = "",
    text: String = ""
) {
    Row {
        Text(
            text = label,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(8.dp)
        )

    }
}


@Composable
fun RadioButtons() {
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)
    val radioButtons = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = true,
                text = yesText
            ),
            ToggleableInfo(
                isChecked = false,
                text = noText
            )
        )
    }
    radioButtons.forEachIndexed { _, info ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                radioButtons.replaceAll {
                    it.copy(
                        isChecked = it.text == info.text
                    )
                }
            }
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                },
            )
            Text(text = info.text)
        }

    }
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 480
)
fun MediumSizedTablet() {
    CoordinatorLoginScreen()
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    CoordinatorLoginScreen()
}

