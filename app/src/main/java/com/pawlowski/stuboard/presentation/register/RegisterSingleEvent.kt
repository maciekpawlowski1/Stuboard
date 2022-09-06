package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.mvi_abstract.SingleEvent

sealed interface RegisterSingleEvent: SingleEvent
{
    object NavigateBack: RegisterSingleEvent
    object RegisterSuccess: RegisterSingleEvent
    data class RegisterFailure(val errorMessage: String): RegisterSingleEvent

}