package com.vanigent.meetingapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.util.Constants

@Composable
fun SectionHeader(
    title: String
) {
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
                    startY = Constants.SEVENTY_PERCENT * boxWidth
                )
            )
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(24.dp)
        )
    }
}