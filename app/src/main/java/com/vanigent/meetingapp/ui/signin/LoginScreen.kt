package com.vanigent.meetingapp.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanigent.meetingapp.R
import com.vanigent.meetingapp.util.Constants

@Composable
fun LoginScreen(
    onLoginButtonClicked: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            LoginFormSection(
                {
                    viewModel.onLoginClicked() // temporal function call
                    onLoginButtonClicked()
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            ImageSection()
        }

    }
}

@Composable
fun LoginFormSection(
    onLoginButtonClicked: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val usernameState by viewModel.usernameState.collectAsStateWithLifecycle()
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    var isPasswordVisible by remember { mutableStateOf(false) }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.7f)
                .fillMaxHeight()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App logo",
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(all = 24.dp)
            )

            // Username textfield
            OutlinedTextField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                label = { Text("Username") },
                value = usernameState.username,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Username Icon"
                    )
                },
                onValueChange = { newUsername ->
                    viewModel.onUsernameTextChanged(newUsername)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password textfield
            OutlinedTextField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                label = { Text("Password") },
                value = passwordState.password,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = trailingIcon,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { newPassword ->
                    viewModel.onPasswordTextChanged(newPassword)
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.vanigent_light_green),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(20.dp),
                onClick = { onLoginButtonClicked() }
            ) {
                Text(text = "SIGN IN")
            }

        }
    }
}

@Composable
fun ImageSection() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var boxWidth by remember { mutableStateOf(0F) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates -> // gets the actual height of the device
                    boxWidth = coordinates.size.width.toFloat()
                }
                .background(
                    Brush.verticalGradient( // brush is used for various kinds of gradients
                        colors = listOf(
                            colorResource(id = R.color.vanigent_light_green),
                            colorResource(id = R.color.vanigent_dark_green),

                            ),
                        startY = Constants.SEVENTY_PERCENT * boxWidth
                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo_white),
                contentDescription = "App logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
                    .padding(all = 24.dp)
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 480
)
fun MediumSizedTablet() {
    LoginScreen(
        onLoginButtonClicked = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 840
)
fun ExpandedSizedTablet() {
    LoginScreen(
        onLoginButtonClicked = {}
    )
}

