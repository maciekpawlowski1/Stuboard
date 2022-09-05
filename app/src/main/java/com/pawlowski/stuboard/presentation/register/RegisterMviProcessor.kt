package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.ui.register_screen.AccountType
import com.pawlowski.stuboard.ui.register_screen.RegisterScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterMviProcessor @Inject constructor(

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
                                state.copy(currentScreen = RegisterScreenType.THIRD_NORMAL)
                        }
                        else{
                            TODO()
                        }
                    }
                    is RegisterIntent.ClearPasswordsInput -> {
                        state.copy(password = "", repeatedPassword = "")
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