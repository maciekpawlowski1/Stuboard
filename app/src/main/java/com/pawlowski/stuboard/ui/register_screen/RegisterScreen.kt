package com.pawlowski.stuboard.ui.register_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.presentation.register.*

const val ANIMATION_TIME = 1000

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: IRegisterMviProcessor = hiltViewModel<RegisterMviProcessor>()
) {
    BackHandler {
        viewModel.sendIntent(RegisterIntent.PreviousClicked)
    }

    LaunchedEffect(true)
    {
        viewModel.singleEvent.collect { event ->
            when(event) {
                is RegisterSingleEvent.NavigateBack -> {
                    onNavigateBack.invoke()
                }
            }
        }
    }

    val uiState = viewModel.viewState.collectAsState()
    val selectedAccountTypeState = derivedStateOf {
        uiState.value.accountType
    }


    val currentScreenState = derivedStateOf {
        uiState.value.currentScreen
    }

    val nameState = derivedStateOf { uiState.value.name }
    val nameErrorState = derivedStateOf { uiState.value.nameError }
    val surnameErrorState = derivedStateOf { uiState.value.surnameError }
    val surnameState = derivedStateOf { uiState.value.surname }
    val emailState = derivedStateOf { uiState.value.email }
    val emailErrorState = derivedStateOf { uiState.value.emailError }
    val passwordState = derivedStateOf { uiState.value.password }
    val repeatedPasswordState = derivedStateOf { uiState.value.repeatedPassword }
    val showPasswordPreviewState = derivedStateOf { uiState.value.showPasswordPreview }
    val showRepeatedPasswordPreviewState =
        derivedStateOf { uiState.value.showRepeatedPasswordPreview }
    val passwordErrorState = derivedStateOf { uiState.value.passwordError }
    val repeatedPasswordErrorState = derivedStateOf { uiState.value.repeatedPasswordError }


    val horizontalPadding = 10.dp
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterScreenHeader(
            backIconHorizontalPadding = horizontalPadding - 5.dp,
            onNavigateBack = {
                onNavigateBack.invoke()
            }
        )

        ProgressBarWithLabel(
            modifier = Modifier.padding(horizontal = horizontalPadding),
            labelTextValue = {
                currentScreenState.value.progressText
            }, progress = {
                currentScreenState.value.progress
            }
        )

        Spacer(modifier = Modifier.height(15.dp))
        RegisterLottieAnimation()

        Spacer(modifier = Modifier.height(15.dp))

        AnimatedContent(targetState = currentScreenState.value,
            transitionSpec = {
                if (targetState.progress > initialState.progress) {
                    slideInHorizontally(
                        animationSpec = tween(ANIMATION_TIME),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) with
                            slideOutHorizontally(
                                animationSpec = tween(ANIMATION_TIME),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                } else {
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
            when (screen) {
                RegisterScreenType.FIRST_BOTH -> {
                    RegisterScreenContent1(
                        horizontalPadding = horizontalPadding,
                        selectedAccountType = selectedAccountTypeState.value,
                        onAccountTypeSelected = {
                            viewModel.sendIntent(
                                RegisterIntent.ChangeAccountType(
                                    it
                                )
                            )
                        },
                        onNextClick = { viewModel.sendIntent(RegisterIntent.NextClicked) },
                    )
                }
                RegisterScreenType.SECOND_NORMAL -> {
                    RegisterScreenContent2Normal(
                        horizontalPadding = horizontalPadding,
                        onNextClick = {
                            viewModel.sendIntent(RegisterIntent.NextClicked)
                        },
                        onPreviousClick = {
                            viewModel.sendIntent(RegisterIntent.PreviousClicked)
                        },
                        nameValue = { nameState.value },
                        onNameChange = { viewModel.sendIntent(RegisterIntent.ChangeNameInputValue(it)) },
                        surnameValue = { surnameState.value },
                        emailValue = { emailState.value },
                        onSurnameChange = {
                            viewModel.sendIntent(
                                RegisterIntent.ChangeSurnameInputValue(
                                    it
                                )
                            )
                        },
                        onEmailChange = {
                            viewModel.sendIntent(
                                RegisterIntent.ChangeMailInputValue(
                                    it
                                )
                            )
                        },
                        emailErrorText = {
                            emailErrorState.value
                        },
                        nameErrorText = {
                            nameErrorState.value
                        },
                        surnameErrorText = {
                            surnameErrorState.value
                        }
                    )
                }
                RegisterScreenType.SECOND_ORGANISATION -> {

                }
                RegisterScreenType.THIRD_NORMAL -> {
                    RegisterScreenContent3Normal(
                        horizontalPadding = horizontalPadding,
                        onCreateAccountClick = { viewModel.sendIntent(RegisterIntent.CreateAccountClicked) },
                        onPreviousClick = {
                            viewModel.sendIntent(RegisterIntent.PreviousClicked)

                        },
                        password = { passwordState.value },
                        repeatedPassword = { repeatedPasswordState.value },
                        onPasswordChange = {
                            viewModel.sendIntent(
                                RegisterIntent.ChangePasswordInputValue(
                                    it
                                )
                            )
                        },
                        onRepeatedPasswordChange = {
                            viewModel.sendIntent(
                                RegisterIntent.ChangeRepeatedPasswordInputValue(
                                    it
                                )
                            )
                        },
                        showPasswordPreview = { showPasswordPreviewState.value },
                        showRepeatedPasswordPreview = { showRepeatedPasswordPreviewState.value },
                        changePasswordPreview = { viewModel.sendIntent(RegisterIntent.ChangePasswordVisibility) },
                        changeRepeatedPasswordPreview = { viewModel.sendIntent(RegisterIntent.ChangeRepeatedPasswordVisibility) },
                        passwordError = { passwordErrorState.value },
                        repeatedPasswordError = { repeatedPasswordErrorState.value }
                    )
                }
                RegisterScreenType.THIRD_ORGANISATION -> {

                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(viewModel = object : IRegisterMviProcessor() {
        override fun initialState(): RegisterUiState {
            return RegisterUiState()
        }

        override val reducer: Reducer<RegisterUiState, RegisterIntent>
            get() = TODO("Not yet implemented")

        override suspend fun handleIntent(
            intent: RegisterIntent,
            state: RegisterUiState
        ): RegisterIntent? {
            TODO("Not yet implemented")
        }

    })
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview2() {
    RegisterScreen(viewModel = object : IRegisterMviProcessor() {
        override fun initialState(): RegisterUiState {
            return RegisterUiState(currentScreen = RegisterScreenType.SECOND_NORMAL)
        }

        override val reducer: Reducer<RegisterUiState, RegisterIntent>
            get() = TODO("Not yet implemented")

        override suspend fun handleIntent(
            intent: RegisterIntent,
            state: RegisterUiState
        ): RegisterIntent? {
            TODO("Not yet implemented")
        }

    })
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview3() {
    RegisterScreen(viewModel = object : IRegisterMviProcessor() {
        override fun initialState(): RegisterUiState {
            return RegisterUiState(currentScreen = RegisterScreenType.THIRD_NORMAL)
        }

        override val reducer: Reducer<RegisterUiState, RegisterIntent>
            get() = TODO("Not yet implemented")

        override suspend fun handleIntent(
            intent: RegisterIntent,
            state: RegisterUiState
        ): RegisterIntent? {
            TODO("Not yet implemented")
        }

    })
}