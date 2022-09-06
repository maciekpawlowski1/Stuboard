package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.presentation.use_cases.LogInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginMviProcessor @Inject constructor(
    private val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
): ILoginMviProcessor() {

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
                    is LoginIntent.ChangeVisibilityOfPassword -> {
                        state.copy(showPasswordPreview = !state.showPasswordPreview)
                    }
                    is LoginIntent.LoginClick -> {
                        state.copy(isLoading = true)
                    }
                    is LoginIntent.StopLoading -> {
                        state.copy(isLoading = false)
                    }
                    is LoginIntent.ClearLoginInputs -> {
                        LoginUiState()
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
            is LoginIntent.LoginClick -> {
                val loginResult = logInWithEmailAndPasswordUseCase(state.email.trim(), state.password)
                return when (loginResult) {
                    is AuthenticationResult.Success -> {
                        triggerSingleEvent(LoginSingleEvent.LoginSuccess)
                        LoginIntent.ClearLoginInputs
                    }
                    is AuthenticationResult.Failure -> {
                        triggerSingleEvent(LoginSingleEvent.LoginFailure(loginResult.errorMessage?:"Login failed!"))
                        LoginIntent.StopLoading
                    }
                }
            }
            else -> null
        }
    }
}