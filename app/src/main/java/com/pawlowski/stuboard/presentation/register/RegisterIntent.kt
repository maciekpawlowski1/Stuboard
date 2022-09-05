package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.mvi_abstract.Intent
import com.pawlowski.stuboard.ui.register_screen.AccountType

sealed interface RegisterIntent: Intent {
    object NextClicked: RegisterIntent
    object PreviousClicked: RegisterIntent
    object CreateAccountClicked: RegisterIntent
    data class ChangeAccountType(val newType: AccountType): RegisterIntent
    data class ChangeMailInputValue(val newValue: String): RegisterIntent
    data class ChangeNameInputValue(val newValue: String): RegisterIntent
    data class ChangeSurnameInputValue(val newValue: String): RegisterIntent
    data class ChangePasswordInputValue(val newValue: String): RegisterIntent
    data class ChangeRepeatedPasswordInputValue(val newValue: String): RegisterIntent
    object ChangePasswordVisibility: RegisterIntent


}
