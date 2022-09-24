package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow

fun interface ObserveLogInStateUseCase: () -> Flow<AppLoginState>