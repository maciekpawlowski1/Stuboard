package com.pawlowski.stuboard.presentation.login

import org.orbitmvi.orbit.ContainerHost

interface ILoginMviViewModel: ContainerHost<LoginUiState, LoginSingleEvent> {
    fun changeEmailInput(newValue: String)
    fun changePasswordInput(newValue: String)
    fun changeVisibilityOfPassword()
    fun login()
    fun openRegisterScreen()
}