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
import timber.log.Timber

@Composable
fun RadioButtons(
    selectedOption: Boolean?,
    clearSelection: Boolean,
    onRadioButtonSelected: (Boolean?) -> Unit,
) {
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)

    Timber.d("selectedOption - $selectedOption")
    Timber.d("clearSelection - $clearSelection")

    val radioButtons = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = selectedOption == true && yesText == "Yes",
                text = yesText
            ),
            ToggleableInfo(
                isChecked = selectedOption == false && noText == "No",
                text = noText
            )
        )
    }

    if (clearSelection) {
        // Clear the selection
        radioButtons.forEach { it.isChecked = false }
    } else {
        // Update the selection based on the selectedOption
        radioButtons.forEach { info ->
            info.isChecked = when (info.text) {
                yesText -> selectedOption == true
                noText -> selectedOption == false
                else -> false
            }
        }
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
                onRadioButtonSelected(info.text == yesText)
            }
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    onRadioButtonSelected(info.text == yesText)
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