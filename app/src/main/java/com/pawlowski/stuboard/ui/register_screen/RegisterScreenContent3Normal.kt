package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun RegisterScreenContent3Normal(
    horizontalPadding: Dp,
    password: () -> String,
    repeatedPassword: () -> String,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    showPasswordPreview: () -> Boolean,
    showRepeatedPasswordPreview: () -> Boolean,
    changePasswordPreview: () -> Unit,
    changeRepeatedPasswordPreview: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onPreviousClick: () -> Unit,
    passwordError: () -> UiText?,
    repeatedPasswordError: () -> UiText?,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        //TODO: Add some vertical scroll or something to textFields will be visible when keyboard appears
        //TODO: But remember about footer buttons
        val passwordErrorVal = passwordError()?.asString()
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = password.invoke(),
            label = { Text(text = "Hasło") },
            onValueChange = { onPasswordChange.invoke(it) },
            visualTransformation = if (!showPasswordPreview.invoke())
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = ""
                )
            },
            trailingIcon = {
                Icon(painter = painterResource(
                    id = if (showPasswordPreview.invoke())
                        R.drawable.visibility_on_icon
                    else
                        R.drawable.visibility_off_icon
                ),
                    contentDescription = "",
                    modifier = Modifier.clickable { changePasswordPreview.invoke() }
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            isError = passwordErrorVal != null,
        )

        if (passwordErrorVal != null) {
            Text(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                text = passwordErrorVal,
                color = MaterialTheme.colors.error
            )
        }

        val repeatedPasswordErrorVal = repeatedPasswordError()?.asString()

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = repeatedPassword.invoke(),
            label = { Text(text = "Powtórz hasło") },
            onValueChange = { onRepeatedPasswordChange.invoke(it) },
            trailingIcon = {
                Icon(painter = painterResource(
                    id = if (showRepeatedPasswordPreview.invoke())
                        R.drawable.visibility_on_icon
                    else
                        R.drawable.visibility_off_icon
                ),
                    contentDescription = "",
                    modifier = Modifier.clickable { changeRepeatedPasswordPreview.invoke() }
                )
            },
            visualTransformation = if (!showRepeatedPasswordPreview.invoke())
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            isError = repeatedPasswordErrorVal != null,
        )

        if (repeatedPasswordErrorVal != null) {
            Text(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding),
                text = repeatedPasswordErrorVal,
                color = MaterialTheme.colors.error
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 15.dp),
            text = "* Hasło musi się składać z przynajmniej 6 znaków i musi zawierać przynajmniej jedną małą literę, dużą literę oraz cyfrę",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = 10.dp),
            onClick = {
                keyboardController?.hide()
                onCreateAccountClick.invoke()
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Utwórz konto", color = Color.White)
        }


        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            onClick = { onPreviousClick.invoke() },
        ) {
            Text(text = "Poprzednia", color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

}