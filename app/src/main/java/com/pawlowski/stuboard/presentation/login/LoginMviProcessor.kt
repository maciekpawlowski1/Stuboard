package com.pawlowski.stuboard.presentation.login

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginMviProcessor @Inject constructor(): ILoginMviProcessor() {

    override fun initialState(): LoginUiState = LoginUiState()

    override val reducer: Reducer<LoginUiState, LoginIntent>
        get() = object: Reducer<LoginUiState, LoginIntent> {
            override fun reduce(state: LoginUiState, intent: LoginIntent): LoginUiState {
                return when(intent) {
                    is LoginIntent.ChangeEmailInputValue -> {
                        state.copy(email = intent.newValue)
                    }
                    is LoginIntent.ChangePasswordInputValue -> {
                        state.copy(password = intent.newValue)
                    }
                    else -> {
                        state
                    }
                }
            }
        }

    override suspend fun handleIntent(intent: LoginIntent, state: LoginUiState): LoginIntent? {
        return when(intent) {
            is LoginIntent.ChangeEmailInputValue -> null
            is LoginIntent.ChangePasswordInputValue -> null
            is LoginIntent.RegisterClick -> {
                triggerSingleEvent(LoginSingleEvent.NavigateToRegisterScreen)
                null
            }
            else -> null
        }
    }
}