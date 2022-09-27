package com.pawlowski.stuboard.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetLogInStateUseCase
import com.pawlowski.stuboard.presentation.use_cases.ObserveLogInStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLogInStateUseCase: GetLogInStateUseCase,
): ViewModel() {

    fun getLoginState() = getLogInStateUseCase()
}