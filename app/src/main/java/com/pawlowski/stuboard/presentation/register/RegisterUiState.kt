package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.mvi_abstract.State
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.register_screen.AccountType
import com.pawlowski.stuboard.ui.register_screen.RegisterScreenType

data class RegisterUiState(
    val currentScreen: RegisterScreenType = RegisterScreenType.FIRST_BOTH,
    val accountType: AccountType = AccountType.NORMAL,
    val name: String = "",
    val nameError: UiText? = null,
    val surname: String = "",
    val surnameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val organisationName: String = "",
    val organisationNameError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: UiText? = null,
): State
