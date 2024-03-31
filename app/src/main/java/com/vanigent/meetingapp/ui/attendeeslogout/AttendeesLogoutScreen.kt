package com.vanigent.meetingapp.ui.attendeeslogout

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.common.CustomCard
import com.vanigent.meetingapp.ui.common.LazyTablePinnedScreen
import com.vanigent.meetingapp.ui.common.SectionHeader
import com.vanigent.meetingapp.ui.coordinatorlogin.components.ActionButton
import com.vanigent.meetingapp.ui.coordinatorlogin.components.LabeledTextRow

@Composable
fun AttendeesLogoutScreen(
    viewModel: AttendeesLogoutViewModel = hiltViewModel()
) {

    val meetingState by viewModel.meetingState.collectAsStateWithLifecycle()
    val numberOfAttendees by viewModel.numberOfAttendees.collectAsStateWithLifecycle()
    val costOfMeal by viewModel.costOfMeal.collectAsStateWithLifecycle()
    val numberOfThoseWhoAte by viewModel.numberOfThoseWhoAte.collectAsStateWithLifecycle()
    val averageCostOfMeal by viewModel.averageCostOfMeal.collectAsStateWithLifecycle()
    val commentsState by viewModel.commentsState.collectAsStateWithLifecycle()
    val isCostPerAttendeeWithinLimit by viewModel.isCostPerAttendeeWithinLimit.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.weight(0.6f)) {

            CustomCard(modifier = Modifier.weight(1f)) {

                SectionHeader(title = stringResource(R.string.summary_of_program))

                meetingState.meeting?.let {
                    LazyTablePinnedScreen(
                        onBackClick = {},
                        meeting = it
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

        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            SummarySection(
                numberOfAttendees = numberOfAttendees,
                costOfMeal = costOfMeal,
                numberOfThoseWhoAte = numberOfThoseWhoAte,
                averageCostOfMeal = averageCostOfMeal
            )

            CustomCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight(),
                contentColor = Color.Red,
                isVisible = !isCostPerAttendeeWithinLimit
            ) {
                Text(
                    text = stringResource(R.string.add_missing_attendees),
                    color = Color.White
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                label = { Text(stringResource(id = R.string.add_comments)) },
                value = commentsState.comment,
                minLines = 4,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    viewModel.onCommentsChanged(it)
                },
                isError = commentsState.isValid?.not() == true
            )

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

