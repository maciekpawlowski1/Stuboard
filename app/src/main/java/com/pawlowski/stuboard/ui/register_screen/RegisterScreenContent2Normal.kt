package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.theme.Green

@Composable
internal fun RegisterScreenContent2Normal(
    horizontalPadding: Dp,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    nameValue: () -> String,
    nameErrorText: () -> UiText?,
    surnameValue: () -> String,
    surnameErrorText: () -> UiText?,
    emailValue: () -> String,
    emailErrorText: () -> UiText?,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        val focusManager = LocalFocusManager.current
        //TODO: Add some vertical scroll or something to textFields will be visible when keyboard appears
        //TODO: But remember about footer buttons

        val nameErrorTextValue = nameErrorText()?.asString()
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = nameValue(),
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Imię") },
            onValueChange = { onNameChange.invoke(it) },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
            ),
            isError = nameErrorTextValue != null,
        )
        if (nameErrorTextValue != null) {
            Text(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                text = nameErrorTextValue,
                color = MaterialTheme.colors.error
            )
        }

        val surnameErrorTextValue = surnameErrorText()?.asString()
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = surnameValue(),
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Nazwisko") },
            onValueChange = { onSurnameChange.invoke(it) },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
            ),
            isError = surnameErrorTextValue != null,
        )
        if (surnameErrorTextValue != null) {
            Text(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                text = surnameErrorTextValue,
                color = MaterialTheme.colors.error
            )
        }

        val emailErrorTextVal = emailErrorText.invoke()?.asString()
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = emailValue(),
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Adres e-mail") },
            onValueChange = { onEmailChange.invoke(it) },
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailErrorTextVal != null,
        )
        if (emailErrorTextVal != null) {
            Text(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                text = emailErrorTextVal,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = 10.dp),
            onClick = { onNextClick.invoke() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Dalej", color = Color.White)
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