package com.pawlowski.stuboard.presentation.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.domain.models.Response
import com.pawlowski.stuboard.presentation.use_cases.FirebaseSignInWithGoogleUseCase
import com.pawlowski.stuboard.presentation.use_cases.LogInWithEmailAndPasswordUseCase
import com.pawlowski.stuboard.presentation.use_cases.OneTapSignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginMviViewModel @Inject constructor(
    private val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
    private val oneTapClient: SignInClient,
    private val oneTapSignInWithGoogleUseCase: OneTapSignInWithGoogleUseCase,
    private val firebaseSignInWithGoogleUseCase: FirebaseSignInWithGoogleUseCase,
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

    override fun oneTapSignIn() = intent(registerIdling = false) {
        oneTapSignInWithGoogleUseCase().collect {
            reduce {
                state.copy(oneTapSignInResponse = it)
            }
        }
    }

    override fun signInFromIntent(intent: Intent) = intent {
        try {
            val credentials = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credentials.googleIdToken
            val googleCredentials = getCredential(googleIdToken, null)
            signInWithGoogle(googleCredentials)
        } catch (e: ApiException) {
            e.printStackTrace()
        }

    }

    private fun signInWithGoogle(googleCredential: AuthCredential) = intent(registerIdling = false) {
        firebaseSignInWithGoogleUseCase(googleCredential).collect { response ->
            when(response)
            {
                is Response.Success -> {
                    response.data?.let {
                        postSideEffect(LoginSingleEvent.LoginSuccess)
                        reduce {
                            LoginUiState()
                        }
                    }
                }
                is Response.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is Response.Failure -> {
                    postSideEffect(LoginSingleEvent.LoginFailure(response.e.localizedMessage?:"Login failed"))
                    reduce { state.copy(isLoading = false) }
                }
            }

        }
    }


}