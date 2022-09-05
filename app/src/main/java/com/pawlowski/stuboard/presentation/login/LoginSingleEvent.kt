package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.mvi_abstract.SingleEvent

sealed interface LoginSingleEvent: SingleEvent
{
    object NavigateToRegisterScreen
}