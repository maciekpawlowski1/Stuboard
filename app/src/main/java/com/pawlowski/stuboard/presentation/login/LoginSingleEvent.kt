package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.utils.UiText


sealed interface LoginSingleEvent
{
    object NavigateToRegisterScreen: LoginSingleEvent
    object LoginSuccess: LoginSingleEvent
    data class LoginFailure(val errorMessage: UiText): LoginSingleEvent
}