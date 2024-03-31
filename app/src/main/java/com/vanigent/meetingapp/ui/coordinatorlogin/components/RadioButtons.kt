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
    selectedOption: Boolean?,
    onRadioButtonSelected: (Boolean?) -> Unit
) {
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)

    val resetRadioButtons = selectedOption == null

    val radioButtons = remember {
        mutableStateListOf(
            ToggleableInfo(
//                isChecked = false,
                isChecked = selectedOption == true && yesText == "Yes",
                text = yesText
            ),
            ToggleableInfo(
                isChecked = selectedOption == true && noText == "No",
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
                if (!resetRadioButtons) {
                    onRadioButtonSelected(info.text == yesText)
                }
            }
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    if (!resetRadioButtons) {
                        onRadioButtonSelected(info.text == yesText)
                    }
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