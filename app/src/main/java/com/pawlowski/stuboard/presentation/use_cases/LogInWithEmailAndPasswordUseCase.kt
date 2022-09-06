package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.data.authentication.AuthenticationResult

fun interface LogInWithEmailAndPasswordUseCase: suspend (String, String) -> AuthenticationResult