package com.pawlowski.stuboard.presentation.login

sealed class LoginUiAction {
    data class ChangeEmailInputValue(val newValue: String): LoginUiAction()
    data class ChangePasswordInputValue(val newValue: String): LoginUiAction()
    object ChangeVisibilityOfPassword
    object LoginClick
    object LoginByGoogleClick
    object TryWithoutLoggingInClick
}
