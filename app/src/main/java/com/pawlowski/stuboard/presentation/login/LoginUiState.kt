package com.pawlowski.stuboard.presentation.login

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.pawlowski.stuboard.domain.models.Response
import com.pawlowski.stuboard.presentation.utils.UiText

data class LoginUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val showPasswordPreview: Boolean = false,
    val isLoading: Boolean = false,
    val oneTapSignInResponse: Response<BeginSignInResult> = Response.Success(null)
)
