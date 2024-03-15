package com.vanigent.meetingapp.ui.attendeeslogout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(0.6f)
        ) {

            CustomCard {

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
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                onClick = {
//                    viewModel.performFieldValidations()

                }
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

