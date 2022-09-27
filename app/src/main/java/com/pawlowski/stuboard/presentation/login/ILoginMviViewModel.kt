package com.pawlowski.stuboard.presentation.login

import android.content.Intent
import org.orbitmvi.orbit.ContainerHost

interface ILoginMviViewModel: ContainerHost<LoginUiState, LoginSingleEvent> {
    fun changeEmailInput(newValue: String)
    fun changePasswordInput(newValue: String)
    fun changeVisibilityOfPassword()
    fun login()
    fun openRegisterScreen()
    fun oneTapSignIn()
    fun signInFromIntent(intent: Intent)
    fun logInAnonymously()
}