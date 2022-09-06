package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.mvi_abstract.Intent

sealed class LoginIntent: Intent {
    data class ChangeEmailInputValue(val newValue: String): LoginIntent()
    data class ChangePasswordInputValue(val newValue: String): LoginIntent()
    object ChangeVisibilityOfPassword: LoginIntent()
    object RegisterClick: LoginIntent()
    object LoginClick: LoginIntent()
    object LoginByGoogleClick: LoginIntent()
    object TryWithoutLoggingInClick: LoginIntent()
    object StopLoading: LoginIntent()
}
