package com.vanigent.meetingapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.theme.MeetingAppTheme

@Composable
fun ProgressIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = colorResource(id = R.color.vanigent_dark_green),
            strokeWidth = Dp(value = 2.5F)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIndicator() {
    MeetingAppTheme() {
        ProgressIndicator()
    }
}
