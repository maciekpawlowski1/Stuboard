package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.mvi_abstract.SingleEvent

sealed interface LoginSingleEvent: SingleEvent
{
    object NavigateToRegisterScreen: LoginSingleEvent
    object LoginSuccess: LoginSingleEvent
    data class LoginFailure(val errorMessage: String): LoginSingleEvent
}