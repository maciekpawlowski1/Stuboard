package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.mvi_abstract.State
import com.pawlowski.stuboard.presentation.utils.UiText

data class LoginUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val showPasswordPreview: Boolean = false
): State
