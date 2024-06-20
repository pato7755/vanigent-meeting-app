package com.vanigent.meetingapp.ui.attendeeslogin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.ui.attendeeslogin.components.DrawingCanvas
import com.vanigent.meetingapp.ui.attendeeslogin.components.ExposedDropdownMenu
import com.vanigent.meetingapp.ui.attendeeslogin.components.Line
import com.vanigent.meetingapp.ui.attendeeslogin.components.PasswordDialog
import com.vanigent.meetingapp.ui.common.ProgressIndicator
import com.vanigent.meetingapp.ui.common.SectionHeader
import com.vanigent.meetingapp.ui.coordinatorlogin.components.ActionButton
import com.vanigent.meetingapp.ui.coordinatorlogin.components.RadioButtons
import com.vanigent.meetingapp.util.Constants.SEVENTY_PERCENT
import com.vanigent.meetingapp.util.Constants.THIRTY_PERCENT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun AttendeesLoginScreen(
    viewModel: AttendeesLoginViewModel = hiltViewModel(),
    onContinueButtonPressed: (String) -> Unit
) {
    val firstNameState by viewModel.firstNameState.collectAsStateWithLifecycle()
    val lastNameState by viewModel.lastNameState.collectAsStateWithLifecycle()
    val pidState by viewModel.pidState.collectAsStateWithLifecycle()
    val professionalDesignationState by viewModel.professionalDesignation.collectAsStateWithLifecycle()
    val signatureLines = viewModel.signatureLines
    val signatureBitmap by viewModel.signatureBitmap.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogVisibility.collectAsStateWithLifecycle()
    val snackbarState by viewModel.snackbarVisibility.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val isFormBlank by viewModel.isFormBlankState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectedRadioButtonOption = viewModel.radioButtonSelection.collectAsStateWithLifecycle()
    val passwordState by viewModel.coordinatorPassword.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val meetingId by viewModel.meetingId.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(SEVENTY_PERCENT)
                    .padding(paddingValues)
            ) {

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(size = 16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            colors = CardDefaults.cardColors(
                                contentColor = colorResource(id = R.color.vanigent_light_green),
                                containerColor = colorResource(id = R.color.white)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Spacer(modifier = Modifier.height(16.dp))

                                ExposedDropdownMenu(
                                    selectedOption = professionalDesignationState.professionalDesignation,
                                    optionsProvider = viewModel::fetchProfessionalDesignations,
                                    onOptionSelected = viewModel::updateDropdownSelectedOption
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(stringResource(id = R.string.first_name)) },
                                    value = firstNameState.firstName,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        capitalization = KeyboardCapitalization.Words,
                                        imeAction = ImeAction.Next
                                    ),
                                    onValueChange = {
                                        viewModel.onFirstNameTextChanged(it)
                                    },
                                    isError = if (isFormBlank) false
                                    else firstNameState.isValid?.not() == true
                                )

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(stringResource(id = R.string.last_name)) },
                                    value = lastNameState.lastName,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        capitalization = KeyboardCapitalization.Words,
                                        imeAction = ImeAction.Next
                                    ),
                                    onValueChange = {
                                        viewModel.onLastNameTextChanged(it)
                                    },
                                    isError = if (isFormBlank) false
                                    else lastNameState.isValid?.not() == true
                                )

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(stringResource(id = R.string.pid)) },
                                    value = pidState.pId,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = {
                                        viewModel.onPidTextChanged(it)
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = stringResource(id = R.string.would_you_like_to_have_something_to_eat),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                RadioButtons(
                                    selectedOption = selectedRadioButtonOption.value,
                                    clearSelection = false
                                ) {
                                    Timber.d("selectedOption - $it")
                                    viewModel.setRadioButtonSelection(it)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (dialogState.isVisible) {
                            PasswordDialog(
                                onDismiss = {
                                    viewModel.toggleDialogVisibility()
                                },
                                onConfirmation = { viewModel.validatePassword() },
                                passwordText = passwordState.password,
                                modifier = Modifier.background(
                                    color = Color.White
                                ),
                                onPasswordTextChanged = viewModel::onPasswordTextChanged

                            )
                        }

                        LaunchedEffect(passwordState.isValid) {
                            when (passwordState.isValid) {
                                true -> {
                                    viewModel.toggleDialogVisibility()
                                    delay(200)
                                    onContinueButtonPressed(meetingId)
                                }
                                false -> {
                                    Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                                }
                                else -> {}
                            }
                        }

                        val errorTextSignature = stringResource(id = R.string.please_add_signature)
                        val errorTextDesignation = stringResource(id = R.string.please_select_designation)

                        ActionButton(
                            text = stringResource(R.string.continue_button),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            onClick = {
                                viewModel.performFieldValidations { _ ->
                                    when {
//                                        professionalDesignationState.isValid != null -> {
                                        /*errorState.isProfessionalDesignationValid != null &&*/ errorState.isProfessionalDesignationValid == false-> {
                                            if (!isFormBlank)
                                                Toast.makeText(context, errorTextDesignation, Toast.LENGTH_SHORT).show()
                                        }
                                        /*errorState.isSignatureValid != null &&*/ errorState.isSignatureValid == false-> {
//                                        signatureBitmap.isValid == false -> {
                                            if (!isFormBlank)
                                                Toast.makeText(context, errorTextSignature, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        )

                    }
                    if (snackbarState.isVisible) {
                        val snackbarMessage = stringResource(R.string.attendees_welcome_text)
                        LaunchedEffect(Unit) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarMessage,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                        viewModel.clearForm()
                        viewModel.toggleSnackbarVisibility()
                    }

                }

            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(THIRTY_PERCENT)
                    .fillMaxWidth()
            ) {
                SignatureSection(
                    lines = signatureLines,
                    onSignatureChanged = viewModel::updateSignatureLines,
                    signatureBitmap = signatureBitmap.bitmap,
                    onSubmitButtonPressed = { viewModel.updateSignature(it) }
                )
            }

            if (loadingState.loadingState) {
                ProgressIndicator()
            }

        }
    }
}

@Composable
fun SignatureSection(
    lines: SnapshotStateList<Line>,
    signatureBitmap: ImageBitmap?,
    onSignatureChanged: (MutableList<Line>) -> Unit,
    onSubmitButtonPressed: (ImageBitmap) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        SectionHeader(stringResource(id = R.string.signature))

        Spacer(modifier = Modifier.height(8.dp))

        DrawingCanvas(
            lines = lines,
            onSignatureChanged = { onSignatureChanged },
            signatureBitmap = signatureBitmap,
            onSubmitButtonPressed = onSubmitButtonPressed
        )

    }

}


@Composable
@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 480
)
fun MediumSizedTablet() {
    AttendeesLoginScreen(
        onContinueButtonPressed = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    AttendeesLoginScreen(
        onContinueButtonPressed = {}
    )
}

