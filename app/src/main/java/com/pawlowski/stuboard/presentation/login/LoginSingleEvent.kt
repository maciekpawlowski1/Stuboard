package com.pawlowski.stuboard.presentation.login


sealed interface LoginSingleEvent
{
    object NavigateToRegisterScreen: LoginSingleEvent
    object LoginSuccess: LoginSingleEvent
    data class LoginFailure(val errorMessage: String): LoginSingleEvent
}