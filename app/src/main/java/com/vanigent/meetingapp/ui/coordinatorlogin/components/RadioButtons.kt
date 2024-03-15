package com.vanigent.meetingapp.ui.coordinatorlogin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.settings.ToggleableInfo

@Composable
fun RadioButtons(
    onRadioButtonSelected: (Boolean) -> Unit
) {
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)
    val radioButtons = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = true,
                text = yesText
            ),
            ToggleableInfo(
                isChecked = false,
                text = noText
            )
        )
    }
    radioButtons.forEachIndexed { _, info ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                radioButtons.replaceAll {
                    it.copy(
                        isChecked = it.text == info.text
                    )
                }
            }
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    onRadioButtonSelected(info.isChecked)
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                },
            )
            Text(text = info.text)
        }

    }
}