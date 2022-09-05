package com.pawlowski.stuboard.presentation.login

import kotlinx.coroutines.flow.StateFlow

interface ILoginViewModel {
    val uiState: StateFlow<LoginUiState>
}