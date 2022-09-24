package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.mvi_abstract.SingleEvent
import com.pawlowski.stuboard.presentation.utils.UiText

sealed interface RegisterSingleEvent: SingleEvent
{
    object NavigateBack: RegisterSingleEvent
    object RegisterSuccess: RegisterSingleEvent
    data class RegisterFailure(val errorMessage: UiText): RegisterSingleEvent
    data class ShowToast(val message: UiText) : RegisterSingleEvent

}