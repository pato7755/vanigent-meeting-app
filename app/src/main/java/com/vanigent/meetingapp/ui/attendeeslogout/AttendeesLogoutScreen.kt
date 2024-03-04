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
import com.vanigent.meetingapp.ui.coordinatorlogin.components.LabeledTextRow

@Composable
fun AttendeesLogoutScreen(
    viewModel: AttendeesLogoutViewModel = hiltViewModel()
) {

    val meetingsState by viewModel.meetingState.collectAsStateWithLifecycle()

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

                LazyTablePinnedScreen(
                    onBackClick = {},
                    meetings = meetingsState.meetings
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            SummarySection()
        }

    }

}

@Composable
fun SummarySection() {
    CustomCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        SectionHeader(title = stringResource(R.string.statistics))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextRow(
                label = stringResource(R.string.number_of_attendees),
                value = "30",
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.number_of_those_who_ate),
                value = "3",
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.cost_of_the_meal),
                value = "4",
                labelModifier = Modifier.weight(1.5f),
                valueModifier = Modifier.weight(0.5f),
                labelColor = Color.Gray
            )
            LabeledTextRow(
                label = stringResource(R.string.average_cost_per_attendee),
                value = "$8",
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

