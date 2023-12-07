@file:OptIn(ExperimentalMaterial3Api::class)

package com.vanigent.meetingapp.ui.coordinatorlogin

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.settings.ToggleableInfo
import com.vanigent.meetingapp.util.Constants.SEVENTY_PERCENT

@Composable
fun CoordinatorLogin(
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            DataEntryForm()

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

                    ReceiptDetails(
                        stringResource(id = R.string.company),
                        "Company"
                    )
                    ReceiptDetails(
                        stringResource(id = R.string.address),
                        "Address"
                    )
                    ReceiptDetails(
                        stringResource(id = R.string.amount),
                        "Amount"
                    )
                }

            }


        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(SEVENTY_PERCENT)
                .fillMaxWidth()
        ) {
            ImageSection()
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
            // Customize the UI for each search result item
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
        query = searchBarState.searchString,
        onQueryChange = viewModel::onSearchTextChanged,
        onSearch = {
            searchResults = viewModel.onSearchAddress(searchBarState.searchString)
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
fun ImageSection(
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {

            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        SectionHeader()

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
fun SectionHeader() {
    var boxWidth by remember { mutableStateOf(0F) }
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .onGloballyPositioned { coordinates -> // gets the actual height of the device
                boxWidth = coordinates.size.width.toFloat()
            }
            .background(
                Brush.verticalGradient( // brush is used for various kinds of gradients
                    colors = listOf(
                        colorResource(id = R.color.vanigent_dark_green),
                        colorResource(id = R.color.vanigent_light_green),

                        ),
                    startY = SEVENTY_PERCENT * boxWidth
                )
            )
    ) {
        Text(
            text = stringResource(id = R.string.capture_receipts),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(24.dp)
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
    radioButtons.forEachIndexed { index, info ->
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
    CoordinatorLogin()
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    CoordinatorLogin()
}

