package com.vanigent.meetingapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.domain.model.Attendee
import com.vanigent.meetingapp.domain.model.Meeting
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableDimensions
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.LazyTablePinConfiguration
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.lazyTablePinConfiguration

@Composable
fun LazyTablePinnedScreen(
    onBackClick: () -> Unit,
    meeting: Meeting
) {

    val columns = 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var settings by remember { mutableStateOf(Settings()) }

        if (meeting.attendee.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyTable(
                pinConfiguration = pinConfiguration(settings),
                dimensions = dimensions(settings = Settings()),
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                items(
                    count = meeting.attendee.size * columns,
                    layoutInfo = {
                        LazyTableItem(
                            column = it % columns,
                            row = it / columns + if (settings.showHeader) 1 else 0,
                        )
                    },
                ) {
                    Cell(
                        attendee = meeting.attendee[it / columns],
                        column = it % columns
                    )
                }

                if (settings.showHeader) {
                    items(
                        count = columns,
                        layoutInfo = {
                            LazyTableItem(
                                column = it % columns,
                                row = 0,
                            )
                        },
                    ) {
                        HeaderCell(column = it)
                    }
                }
            }
        }
    }

}

private fun dimensions(settings: Settings): LazyTableDimensions =
    lazyTableDimensions(
        columnSize = {
            200.dp
        },
        rowSize = {
            48.dp
        }
    )

private fun pinConfiguration(settings: Settings): LazyTablePinConfiguration =
    lazyTablePinConfiguration(
        columns = 0,
        rows = 0,
    )

@Composable
private fun Cell(
    attendee: Attendee,
    column: Int,
) {
    val content = when (column) {
        0 -> {
            attendee.attendeeFirstName
        }

        1 -> {
            attendee.attendeeLastName
        }

        2 -> {
            if (attendee.attendeeWillConsumeFood)
                stringResource(id = R.string.yes)
            else stringResource(id = R.string.no)
        }

        else -> "error"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface)
    ) {
        if (content.isNotEmpty()) {
            Text(text = content)
        }
    }
}

@Composable
private fun HeaderCell(column: Int) {
    val content = when (column) {
        0 -> "First Name"
        1 -> "Last Name"
        2 -> "Food"
        else -> ""
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(colorResource(id = R.color.vanigent_light_green))
            .border(
                width = Dp.Hairline,
                color = Color.Black
            )
    ) {
        Text(
            text = content,
            color = Color.White
        )
    }
}

private data class Settings(
    val showHeader: Boolean = true,
    val showFooter: Boolean = false,
    val pinName: Boolean = false,
    val pinImage: Boolean = false,
)

fun meetings(): List<Meeting> {
    return listOf(
        Meeting(
            officeLocation = "Cook County",
            coordinatorWillConsumeFood = false,
            receipt = listOf(),
            attendee = listOf(
                Attendee(
                    attendeeFirstName = "John",
                    attendeeLastName = "One",
                    attendeePid = "12345",
                    attendeeWillConsumeFood = true,
                    attendeeProfessionalDesignation = "PA",
                    attendeeSignature = "".toByteArray()
                )
            )
        ),
        Meeting(
            officeLocation = "Atlanta",
            coordinatorWillConsumeFood = false,
            receipt = listOf(),
            attendee = listOf(
                Attendee(
                    attendeeFirstName = "John",
                    attendeeLastName = "Two",
                    attendeePid = "339837",
                    attendeeWillConsumeFood = false,
                    attendeeProfessionalDesignation = "Pharm",
                    attendeeSignature = "".toByteArray()
                )
            )
        ),
        Meeting(
            officeLocation = "Miami",
            coordinatorWillConsumeFood = true,
            receipt = listOf(),
            attendee = listOf(
                Attendee(
                    attendeeFirstName = "John",
                    attendeeLastName = "Three",
                    attendeePid = "8877663",
                    attendeeWillConsumeFood = true,
                    attendeeProfessionalDesignation = "NP",
                    attendeeSignature = "".toByteArray()
                )
            )
        ),

        )
}


@Composable
@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 480
)
fun MediumSizedTablet() {
    LazyTablePinnedScreen(
        meeting = meetings()[0],
        onBackClick = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    LazyTablePinnedScreen(
        meeting = meetings()[0],
        onBackClick = {}
    )
}
