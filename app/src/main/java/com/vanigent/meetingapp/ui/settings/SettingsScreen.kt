package com.vanigent.meetingapp.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vanigent.meetingapp.R


data class ToggleableInfo(
    var isChecked: Boolean,
    val text: String
)

@Composable
fun Settings() {
    val darkModeText = stringResource(R.string.dark_mode)
    var switch by remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = false,
                text = darkModeText
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = switch.text)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = switch.isChecked,
            onCheckedChange = { isChecked ->
                switch = switch.copy(isChecked = isChecked)

            },
            thumbContent = {
                if (switch.isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = ""
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = ""
                    )
                }
            }
        )
    }
}