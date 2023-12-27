package com.vanigent.meetingapp.ui.coordinatorlogin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabeledTextRow(
    labelModifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier,
    labelColor: Color = Color.Unspecified,
    valueColor: Color = Color.Unspecified,
    label: String = "",
    value: String = "",
) {
    Row {
        Text(
            text = label,
            modifier = Modifier
                .padding(8.dp)
                .then(labelModifier),
            color = if (labelColor != Color.Unspecified) labelColor else LocalContentColor.current,
        )
        Text(
            text = value,
            modifier = Modifier
                .padding(8.dp)
                .then(valueModifier),
            color = if (valueColor != Color.Unspecified) valueColor else LocalContentColor.current,
        )

    }
}