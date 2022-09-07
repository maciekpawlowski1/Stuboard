package com.pawlowski.stuboard.presentation.login

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.presentation.use_cases.LogInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginMviViewModel @Inject constructor(
    private val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
): ILoginMviViewModel, ViewModel() {

    override val container = container<LoginUiState, LoginSingleEvent>(LoginUiState())

    override fun changeEmailInput(newValue: String) = intent {
        reduce {
            state.copy(email = newValue)
        }
    }

    override fun changePasswordInput(newValue: String) = intent {
        reduce {
            state.copy(password = newValue)
        }
    }

    override fun changeVisibilityOfPassword() = intent {
        reduce {
            state.copy(showPasswordPreview = !state.showPasswordPreview)
        }
    }

    override fun login() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        when(val result = logInWithEmailAndPasswordUseCase(state.email.trim(), state.password)) {
            is AuthenticationResult.Success -> {
                postSideEffect(LoginSingleEvent.LoginSuccess)
                reduce {
                    LoginUiState()
                }
            }
            is AuthenticationResult.Failure -> {
                postSideEffect(LoginSingleEvent.LoginFailure(result.errorMessage?:"Login failed!"))
                reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    override fun openRegisterScreen() = intent {
        postSideEffect(LoginSingleEvent.NavigateToRegisterScreen)
    }
}