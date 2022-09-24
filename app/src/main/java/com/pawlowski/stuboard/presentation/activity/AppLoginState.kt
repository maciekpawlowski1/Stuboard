package com.pawlowski.stuboard.presentation.activity

sealed class AppLoginState {
    object LoggedIn: AppLoginState()
    object LoggedInAnonymously: AppLoginState()
    object NotLoggedIn: AppLoginState()
}
