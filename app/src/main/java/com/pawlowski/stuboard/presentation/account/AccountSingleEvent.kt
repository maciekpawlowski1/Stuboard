package com.pawlowski.stuboard.presentation.account

sealed interface AccountSingleEvent {
    object NavigateToLogIn: AccountSingleEvent
}