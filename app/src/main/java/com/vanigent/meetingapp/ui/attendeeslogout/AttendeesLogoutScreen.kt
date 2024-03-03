package com.vanigent.meetingapp.ui.attendeeslogout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.common.LazyTablePinnedScreen
import com.vanigent.meetingapp.ui.common.TopBar
import com.vanigent.meetingapp.util.Constants

@Composable
fun AttendeesLogoutScreen(
    viewModel: AttendeesLogoutViewModel = hiltViewModel()
) {

//    val snackbarState by viewModel.snackbarVisibility.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val meetingsState by viewModel.meetingState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar(
                title = "label",
//                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

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
                        .weight(Constants.SEVENTY_PERCENT)
                        .padding(paddingValues)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyTablePinnedScreen(
                        onBackClick = {},
                        meetings = meetingsState.meetings
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(Constants.THIRTY_PERCENT)
                    .fillMaxWidth()
            ) {
//                SignatureSection(
//                    lines = signatureLines,
//                    onSignatureChanged = viewModel::updateSignatureLines,
//                    signatureBitmap = signatureBitmap.bitmap,
//                    onSubmitButtonPressed = { viewModel.updateSignature(it) }
//                )
            }

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
    AttendeesLogoutScreen(
//        onContinueButtonPressed = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    AttendeesLogoutScreen(
//        onContinueButtonPressed = {}
    )
}

