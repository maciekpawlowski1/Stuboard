package com.pawlowski.stuboard.ui.login_screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.domain.models.Response
import com.pawlowski.stuboard.presentation.login.ILoginMviViewModel
import com.pawlowski.stuboard.presentation.login.LoginMviViewModel
import com.pawlowski.stuboard.presentation.login.LoginSingleEvent
import com.pawlowski.stuboard.presentation.login.LoginUiState
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext

data class LoginNavigationCallbacks(
    val onNavigateToRegisterScreen: () -> Unit = {},
    val onNavigateToRoot: () -> Unit = {}
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navigationCallbacks: LoginNavigationCallbacks = LoginNavigationCallbacks(),
                viewModel: ILoginMviViewModel = hiltViewModel<LoginMviViewModel>(),
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val emailState = derivedStateOf {
        uiState.value.email
    }

    val passwordState = derivedStateOf {
        uiState.value.password
    }

    val showPasswordState = derivedStateOf {
        uiState.value.showPasswordPreview
    }

    val isLoadingState = derivedStateOf {
        uiState.value.isLoading
    }

    val oneTapResponseState = derivedStateOf {
        uiState.value.oneTapSignInResponse
    }

    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is LoginSingleEvent.NavigateToRegisterScreen -> {
                    navigationCallbacks.onNavigateToRegisterScreen()
                }
                is LoginSingleEvent.LoginSuccess -> {
                    Toast.makeText(context, "Zalogowano pomyślnie!", Toast.LENGTH_LONG).show()
                    navigationCallbacks.onNavigateToRoot()
                }
                is LoginSingleEvent.LoginFailure -> {
                    Toast.makeText(context, event.errorMessage.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    Column(Modifier.verticalScroll(rememberScrollState())) {
        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sign_in_animation))
        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(modifier = Modifier
            .padding(top = 10.dp)
            .height(200.dp), composition = lottieComposition, progress = { progress })

        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp),
            text = "Witaj!",
            fontFamily = montserratFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            text = "Zacznijmy, zaloguj się!",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(CenterHorizontally),
            value = emailState.value,
            label = { Text(text = "Adres e-mail") },
            onValueChange = { viewModel.changeEmailInput(it) },
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = ""
                )
            },
            maxLines = 1,
            singleLine = true,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )


        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(CenterHorizontally),
            value = passwordState.value,
            label = { Text(text = "Hasło") },
            maxLines = 1,
            singleLine = true,
            onValueChange = { viewModel.changePasswordInput(it) },
            visualTransformation = if(!showPasswordState.value)
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
                       id = if (showPasswordState.value)
                           R.drawable.visibility_on_icon
                       else
                           R.drawable.visibility_off_icon
                       ),
                       contentDescription = "",
                       modifier = Modifier.clickable { viewModel.changeVisibilityOfPassword() }
                   )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                viewModel.login()
                keyboardController?.hide()
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
            Text(text = "Zaloguj się", color = Color.White)
        }

        if(isLoadingState.value)
        {
            CircularProgressIndicator(
                Modifier
                    .size(50.dp)
                    .align(CenterHorizontally), color = Green)
            Spacer(modifier = Modifier.height(10.dp))
        }

        HorizontalDividerWithLabelInTheMiddle()
        Spacer(modifier = Modifier.height(10.dp))
        LogInByGoogleButton()
        {
            viewModel.oneTapSignIn()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(modifier = Modifier
            .align(CenterHorizontally)
            .clickable { viewModel.openRegisterScreen() },
            fontFamily = montserratFont,
            text = buildAnnotatedString {
            append("Nie masz konta? ")
            withStyle(style = SpanStyle(color = Green, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.SemiBold))
            {
                append("Zarejestruj się")
            }
        })
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDividerWithLabelInTheMiddle()
        Spacer(modifier = Modifier.height(10.dp))
        ContinueAnonymousButton()
        Spacer(modifier = Modifier.height(10.dp))


        HandleOneTapSignIn(oneTapResponseValue = { oneTapResponseState.value }, handleIntent = { intent ->
            viewModel.signInFromIntent(intent)
        })

    }

}



@Composable
fun HandleOneTapSignIn(oneTapResponseValue: () -> Response<BeginSignInResult>, handleIntent: (Intent) -> Unit)
{
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                handleIntent(it)
            }
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    when(val result = oneTapResponseValue.invoke()) {
        is Response.Success<BeginSignInResult> -> {
            result.data?.let {
                LaunchedEffect(it)
                {
                    launch(it)
                }
            }
        }
        else -> {}
    }
}

@Composable
fun HorizontalDividerWithLabelInTheMiddle()
{
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = "Lub",
            color = MidGrey,
            fontSize = 13.sp,
            fontFamily = montserratFont
        )
        Divider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LogInByGoogleButton(onClick: () -> Unit)
{
    OutlinedButton(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(36.dp)
            .fillMaxWidth(),
        onClick = { onClick.invoke() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier= Modifier.align(CenterStart),
                painter = painterResource(id = R.drawable.google_icon), contentDescription = "")
            Text(modifier= Modifier.align(Center),text = "Zaloguj się przez Google", color = Green)
        }

    }
}

@Composable
fun ContinueAnonymousButton()
{
    OutlinedButton(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(36.dp)
            .fillMaxWidth(),
        onClick = { /*TODO*/ }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier= Modifier.align(CenterStart),
                painter = painterResource(id = R.drawable.incognito_icon), contentDescription = "")
            Text(modifier= Modifier.align(Center),text = "Zacznij bez logowania", color = Green)
        }

    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview()
{
    LoginScreen(viewModel = object : ILoginMviViewModel
    {
        override fun changeEmailInput(newValue: String) {
            TODO("Not yet implemented")
        }
        override fun changePasswordInput(newValue: String) {
            TODO("Not yet implemented")
        }
        override fun changeVisibilityOfPassword() {
            TODO("Not yet implemented")
        }
        override fun login() {
            TODO("Not yet implemented")
        }
        override fun openRegisterScreen() {
            TODO("Not yet implemented")
        }

        override fun oneTapSignIn() {
            TODO("Not yet implemented")
        }

        override fun signInFromIntent(intent: Intent) {
            TODO("Not yet implemented")
        }

        override val container: Container<LoginUiState, LoginSingleEvent>
            get() = object: Container<LoginUiState, LoginSingleEvent> {
                override val settings: Container.Settings
                    get() = TODO("Not yet implemented")
                override val sideEffectFlow: Flow<LoginSingleEvent>
                    get() = flow {}
                override val stateFlow: StateFlow<LoginUiState>
                    get() = MutableStateFlow(LoginUiState())

                override suspend fun orbit(orbitIntent: suspend ContainerContext<LoginUiState, LoginSingleEvent>.() -> Unit) {
                    TODO("Not yet implemented")
                }
            }
    })
}