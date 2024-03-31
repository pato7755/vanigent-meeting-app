package com.vanigent.meetingapp.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vanigent.meetingapp.R

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    contentColor: Color = Color.White,
    content: @Composable () -> Unit,
) {
    if (isVisible) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(size = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                contentColor = colorResource(id = R.color.vanigent_light_green),
                containerColor = contentColor
            ),

            ) {
            content()
        }
    }
}
