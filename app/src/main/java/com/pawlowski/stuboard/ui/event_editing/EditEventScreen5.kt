package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditEventScreen5(
    descriptionInput: () -> String = { "" },
    onDescriptionInputChange: (String) -> Unit = {},
    siteInput: () -> String = { "" },
    onSiteInputChange: (String) -> Unit = {},
    facebookSiteInput: () -> String = { "" },
    onFacebookSiteInputChange: (String) -> Unit = {},
    onMoveToPublishingClick: () -> Unit = {}
)
{
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Opis wydarzenia",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(300.dp),
            value = descriptionInput(),
            singleLine = false,
            onValueChange = onDescriptionInputChange,
            colors = textFieldColors,
            label = { Text(text = "Opis wydarzenia") },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Linki do wydarzenia (opcjonalne):",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = siteInput(),
            singleLine = true,
            onValueChange = onSiteInputChange,
            colors = textFieldColors,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.website_icon),
                    contentDescription = "",
                    tint = Green
                )
                          },
            label = { Text(text = "Link do strony wydarzenia") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = facebookSiteInput(),
            singleLine = true,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.facebook_icon),
                    contentDescription = "",
                )
                          },
            onValueChange = onFacebookSiteInputChange,
            colors = textFieldColors,
            label = { Text(text = "Link do wydarzenia na facebooku") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            onClick = { onMoveToPublishingClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Przejdź do publikacji", color = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Icon(painter = painterResource(id = R.drawable.send_icon), contentDescription = "", tint = Color.White)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditEventScreen5Preview()
{
    EditEventScreen5()
}