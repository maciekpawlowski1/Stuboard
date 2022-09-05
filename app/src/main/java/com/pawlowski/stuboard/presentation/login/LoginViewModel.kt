package com.pawlowski.stuboard.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.validation.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    validateEmailUseCase: ValidateEmailUseCase
): ViewModel(), ILoginViewModel {
    private val initialUiState = LoginUiState()

    //private val uiActions = MutableSharedFlow<LoginUiAction>(extraBufferCapacity = 10)
    private val _uiState = MutableStateFlow(initialUiState)

    override val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()

    fun onAction(action: LoginUiAction)
    {
        viewModelScope.launch(Dispatchers.Main.immediate) {

        }
    }

}