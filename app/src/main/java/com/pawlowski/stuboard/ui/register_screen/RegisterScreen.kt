package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

const val ANIMATION_TIME = 1000

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(onNavigateBack: () -> Unit = {})
{
    val selectedAccountType = remember {
        mutableStateOf(AccountType.NORMAL)
    }

    val currentScreenState = remember {
        mutableStateOf(RegisterScreenType.FIRST_BOTH)
    }


    val horizontalPadding = 10.dp
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier
                    .clickable { onNavigateBack.invoke() }
                    .padding(horizontal = horizontalPadding - 5.dp)
                    .padding(top = 3.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.arrow_back_icon),
                contentDescription = ""
            )
            Text(
                text = "Rejestracja",
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
        }
        Text(
            text = currentScreenState.value.progressText,
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        LinearProgressIndicator(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
            color = Green,
            progress = animateFloatAsState(targetValue = currentScreenState.value.progress).value
        )

        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))
        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(modifier = Modifier
            .padding(top = 15.dp)
            .height(193.dp),
            composition = lottieComposition, 
            progress = { progress })

        Spacer(modifier = Modifier.height(15.dp))

        AnimatedContent(targetState = currentScreenState.value,
        transitionSpec = {
            if(targetState.progress > initialState.progress)
            {
                slideInHorizontally(
                    animationSpec = tween(ANIMATION_TIME),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) with
                    slideOutHorizontally(
                        animationSpec = tween(ANIMATION_TIME),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
            }
            else
            {
                slideInHorizontally(
                    animationSpec = tween(ANIMATION_TIME),
                    initialOffsetX = { fullWidth -> -fullWidth }
                ) with
                        slideOutHorizontally(
                            animationSpec = tween(ANIMATION_TIME),
                            targetOffsetX = { fullWidth -> fullWidth }
                        )
            }
        }) { screen ->
            when(screen)
            {
                RegisterScreenType.FIRST_BOTH -> {
                    RegisterScreenContent1(
                        horizontalPadding = horizontalPadding,
                        selectedAccountType = selectedAccountType.value,
                        onAccountTypeSelected = { selectedAccountType.value = it },
                        onNextClick = { currentScreenState.value = RegisterScreenType.SECOND_NORMAL },
                    )
                }
                RegisterScreenType.SECOND_NORMAL -> {
                    RegisterScreenContent2Normal(horizontalPadding = horizontalPadding,
                        onNextClick = {
                        currentScreenState.value = RegisterScreenType.THIRD_NORMAL
                    },
                        onPreviousClick = {
                            currentScreenState.value = RegisterScreenType.FIRST_BOTH
                        }
                    )
                }
                RegisterScreenType.SECOND_ORGANISATION -> {

                }
                RegisterScreenType.THIRD_NORMAL -> {
                    RegisterScreenContent3Normal(
                        horizontalPadding = horizontalPadding,
                        onCreateAccountClick = { /*TODO*/ },
                        onPreviousClick = {
                            currentScreenState.value = RegisterScreenType.SECOND_NORMAL
                        }
                    )
                }
                RegisterScreenType.THIRD_ORGANISATION -> {

                }
            }
        }

    }
}

enum class AccountType(val accountTypeId: Int) {
    NORMAL(0),
    STUDENT_ORGANISATION(1)
}

enum class RegisterScreenType(val progress: Float, val progressText: String) {
    FIRST_BOTH(0.33f, "1 of 3"),
    SECOND_NORMAL(0.66f, "2 of 3"),
    SECOND_ORGANISATION(0.66f, "2 of 3"),
    THIRD_NORMAL(1f, "3 of 3"),
    THIRD_ORGANISATION(1f, "3 of 3")
}

@Composable
private fun RegisterScreenContent1(horizontalPadding: Dp, selectedAccountType: AccountType, onAccountTypeSelected: (AccountType) -> Unit, onNextClick: () -> Unit)
{
    Column {
        Text(
            text = "Wybierz typ konta",
            fontFamily = montserratFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))
        AccountTypeSwitch(
            modifier = Modifier
                .padding(horizontal = horizontalPadding),
            selectedAccountType = selectedAccountType,
            onAccountTypeSelected = { onAccountTypeSelected.invoke(it) }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            modifier = Modifier.padding(horizontal = horizontalPadding),
            text = "* Konta o typie Organizacja Studencka wymagają  dodatkowej weryfikacji",
            fontFamily = montserratFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            onClick = { onNextClick.invoke() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Dalej", color = Color.White)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

}

@Composable
private fun RegisterScreenContent2Normal(
    horizontalPadding: Dp,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
)
{
    Column {
        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        //TODO: Add some vertical scroll or something to textFields will be visible when keyboard appears
        //TODO: But remember about footer buttons
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = "",
            label = { Text(text = "Imię") },
            onValueChange = {},
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

            )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = "",
            label = { Text(text = "Nazwisko") },
            onValueChange = {},
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

            )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(Alignment.CenterHorizontally),
            value = "",
            label = { Text(text = "Adres e-mail") },
            onValueChange = {},
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

            )
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
            Text(text = "Poprzednia", color= Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

}

@Composable
private fun RegisterScreenContent3Normal(
    horizontalPadding: Dp,
    onCreateAccountClick: () -> Unit,
    onPreviousClick: () -> Unit
)
{
    Column {
        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        //TODO: Add some vertical scroll or something to textFields will be visible when keyboard appears
        //TODO: But remember about footer buttons
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = "",
            label = { Text(text = "Hasło") },
            onValueChange = {},
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = "",
            label = { Text(text = "Powtórz hasło") },
            onValueChange = {},
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

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
            onClick = { onCreateAccountClick.invoke() },
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
            Text(text = "Poprzednia", color= Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

}

@Composable
fun AccountTypeSwitch(modifier: Modifier = Modifier, selectedAccountType: AccountType, onAccountTypeSelected: (AccountType) -> Unit = {})
{
    Card(modifier = modifier
        .height(36.dp)
        .fillMaxWidth()) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedAccountType == AccountType.NORMAL)
                            Green
                        else
                            Color.White
                    )
                    .clickable { onAccountTypeSelected.invoke(AccountType.NORMAL) },
                contentAlignment = Alignment.Center)
            {
                if(selectedAccountType == AccountType.NORMAL)
                {
                    SelectedText(text = "Zwykłe konto")
                }
                else
                {
                    NotSelectedText(text = "Zwykłe konto")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedAccountType == AccountType.NORMAL)
                            Color.White
                        else
                            Green
                    )
                    .clickable { onAccountTypeSelected.invoke(AccountType.STUDENT_ORGANISATION) },
                contentAlignment = Alignment.Center)
            {
                if(selectedAccountType == AccountType.STUDENT_ORGANISATION)
                {
                    SelectedText(text = "Organizacja studencka")
                }
                else
                {
                    NotSelectedText(text = "Organizacja studencka")
                }
            }
        }
    }
}

@Composable
private fun SelectedText(text: String)
{
    Text(
        text = text,
        color = Color.White,
        fontFamily = montserratFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    )
}

@Composable
private fun NotSelectedText(text: String)
{
    Text(
        text = text,
        color = MidGrey,
        fontFamily = montserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview()
{
    RegisterScreen()
}