package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.use_cases.validation.ValidateEmailUseCase
import com.pawlowski.stuboard.presentation.use_cases.validation.ValidateNameOrSurnameUseCase
import com.pawlowski.stuboard.presentation.use_cases.validation.ValidateNewPasswordUseCase
import com.pawlowski.stuboard.presentation.use_cases.validation.ValidateRepeatedPasswordUseCase
import com.pawlowski.stuboard.ui.register_screen.AccountType
import com.pawlowski.stuboard.ui.register_screen.RegisterScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterMviProcessor @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateNewPasswordUseCase: ValidateNewPasswordUseCase,
    private val validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase,
    private val validateNameOrSurnameUseCase: ValidateNameOrSurnameUseCase,
): IRegisterMviProcessor() {

    override fun initialState(): RegisterUiState = RegisterUiState()

    override val reducer: Reducer<RegisterUiState, RegisterIntent> = object : Reducer<RegisterUiState, RegisterIntent>
        {
            override fun reduce(state: RegisterUiState, intent: RegisterIntent): RegisterUiState {
                return when(intent) {
                    is RegisterIntent.ChangeMailInputValue -> {
                        state.copy(email = intent.newValue)
                    }
                    is RegisterIntent.ChangeAccountType -> {
                        state.copy(accountType = intent.newType)
                    }
                    is RegisterIntent.ChangeNameInputValue -> {
                        state.copy(name = intent.newValue)
                    }
                    is RegisterIntent.ChangeSurnameInputValue -> {
                        state.copy(surname = intent.newValue)
                    }
                    is RegisterIntent.ChangePasswordInputValue -> {
                        state.copy(password = intent.newValue)
                    }
                    is RegisterIntent.ChangeRepeatedPasswordInputValue -> {
                        state.copy(repeatedPassword = intent.newValue)
                    }
                    is RegisterIntent.NextClicked -> {
                        val selectedAccountType = state.accountType
                        if(selectedAccountType == AccountType.NORMAL)
                        {
                            if(state.currentScreen == RegisterScreenType.FIRST_BOTH)
                                state.copy(currentScreen = RegisterScreenType.SECOND_NORMAL)
                            else
                            {
                                val emailValidationResult = validateEmailUseCase.invoke(state.email)
                                val nameValidationResult = validateNameOrSurnameUseCase.invoke(state.name)
                                val surnameValidationResult = validateNameOrSurnameUseCase.invoke(state.surname)
                                if(emailValidationResult.isCorrect && nameValidationResult.isCorrect && surnameValidationResult.isCorrect)
                                    state.copy(
                                        currentScreen = RegisterScreenType.THIRD_NORMAL,
                                        emailError = null,
                                        nameError = null,
                                        surnameError = null
                                    )
                                else
                                    state.copy(
                                        emailError = emailValidationResult.errorMessage,
                                        nameError = nameValidationResult.errorMessage,
                                        surnameError = surnameValidationResult.errorMessage
                                    )
                            }
                        }
                        else{
                            TODO()
                        }
                    }
                    is RegisterIntent.ClearPasswordsInput -> {
                        state.copy(password = "", repeatedPassword = "", passwordError = null, repeatedPasswordError = null)
                    }
                    is RegisterIntent.CreateAccountClicked -> {
                        val passwordValidationResult = validateNewPasswordUseCase(state.password)
                        val repeatedPasswordValidationResult = validateRepeatedPasswordUseCase(state.password, state.repeatedPassword)
                        if(passwordValidationResult.isCorrect && repeatedPasswordValidationResult.isCorrect)
                        {
                            //TODO: Register
                            state.copy(repeatedPasswordError = null, passwordError = null)
                        }
                        else
                        {
                            state.copy(passwordError = passwordValidationResult.errorMessage, repeatedPasswordError = repeatedPasswordValidationResult.errorMessage)
                        }
                    }
                    is RegisterIntent.ChangePasswordVisibility -> {
                        state.copy(showPasswordPreview = !state.showPasswordPreview)
                    }
                    is RegisterIntent.ChangeRepeatedPasswordVisibility -> {
                        state.copy(showRepeatedPasswordPreview = !state.showRepeatedPasswordPreview)
                    }
                    is RegisterIntent.PreviousClicked -> {
                        val selectedAccountType = state.accountType
                        if(selectedAccountType == AccountType.NORMAL)
                        {
                            if(state.currentScreen == RegisterScreenType.THIRD_NORMAL)
                                state.copy(currentScreen = RegisterScreenType.SECOND_NORMAL)
                            else
                                state.copy(currentScreen = RegisterScreenType.FIRST_BOTH)
                        }
                        else{
                            TODO()
                        }
                    }
                    else -> {
                        state
                    }
                }
            }
        }

    override suspend fun handleIntent(
        intent: RegisterIntent,
        state: RegisterUiState
    ): RegisterIntent? {
        return when(intent) {
            is RegisterIntent.ChangeAccountType -> null
            is RegisterIntent.PreviousClicked -> RegisterIntent.ClearPasswordsInput
            else -> null
        }
    }
}