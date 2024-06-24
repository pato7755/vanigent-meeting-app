package com.vanigent.meetingapp.ui.attendeeslogout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.common.CustomCard
import com.vanigent.meetingapp.ui.common.LazyTablePinnedScreen
import com.vanigent.meetingapp.ui.common.ProgressIndicator
import com.vanigent.meetingapp.ui.common.SectionHeader
import com.vanigent.meetingapp.ui.coordinatorlogin.components.ActionButton
import com.vanigent.meetingapp.ui.coordinatorlogin.components.LabeledTextRow

@Composable
fun AttendeesLogoutScreen(
    onUploadSuccess: () -> Unit,
    viewModel: AttendeesLogoutViewModel = hiltViewModel()
) {

    val meetingState by viewModel.meetingState.collectAsStateWithLifecycle()
    val numberOfAttendees by viewModel.numberOfAttendees.collectAsStateWithLifecycle()
    val costOfMeal by viewModel.costOfMeal.collectAsStateWithLifecycle()
    val numberOfThoseWhoAte by viewModel.numberOfThoseWhoAte.collectAsStateWithLifecycle()
    val averageCostOfMeal by viewModel.averageCostOfMeal.collectAsStateWithLifecycle()
    val commentsState by viewModel.commentsState.collectAsStateWithLifecycle()
    val isCostPerAttendeeWithinLimit by viewModel.isCostPerAttendeeWithinLimit.collectAsStateWithLifecycle()
    val pdfUploadState by viewModel.pdfUploadState.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.weight(0.6f)) {

            CustomCard(modifier = Modifier.weight(1f)) {

                SectionHeader(title = stringResource(R.string.summary_of_program))

                meetingState.meeting?.let {
                    LazyTablePinnedScreen(
                        onBackClick = {},
                        meeting = it,
                        coordinatorFirstName = meetingState.coordinatorFirstName,
                        coordinatorLastName = meetingState.coordinatorLastName
                    )
                }
            }

            ActionButton(
                text = stringResource(R.string.continue_button),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.onContinuePressed()
                },
                enabled = isCostPerAttendeeWithinLimit,
            )

        }

        Spacer(modifier = Modifier.width(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    SummarySection(
                        numberOfAttendees = numberOfAttendees,
                        costOfMeal = costOfMeal,
                        numberOfThoseWhoAte = numberOfThoseWhoAte,
                        averageCostOfMeal = averageCostOfMeal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentColor = Color.Red,
                        isVisible = !isCostPerAttendeeWithinLimit
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(R.string.add_missing_attendees),
                            color = Color.White,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        label = { Text(stringResource(id = R.string.add_comments)) },
                        value = commentsState.comment,
                        minLines = 4,
                        textStyle = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = {
                            viewModel.onCommentsChanged(it)
                        },
                        isError = commentsState.isValid?.not() == true
                    )

                }
            }
        }

        if (loadingState.loadingState) {
            ProgressIndicator()
        }

        LaunchedEffect(pdfUploadState.uploadState) {
            pdfUploadState.uploadState?.let {
                if (it) {
                    onUploadSuccess()
                } else {
                    Toast.makeText(context, "Meeting PDF upload unsuccessful", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}

@Composable
fun SummarySection(
    numberOfAttendees: Int,
    costOfMeal: Double,
    numberOfThoseWhoAte: Int,
    averageCostOfMeal: Double,
) {


    CustomCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SectionHeader(title = stringResource(R.string.statistics))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextRow(
                label = stringResource(R.string.number_of_attendees),
                value = numberOfAttendees.toString(),
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.number_of_those_who_ate),
                value = numberOfThoseWhoAte.toString(),
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.cost_of_the_meal),
                value = "$${costOfMeal}",
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.average_cost_per_attendee),
                value = "$${averageCostOfMeal}",
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )

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
        onUploadSuccess = {}
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
        onUploadSuccess = {}
    )
}

