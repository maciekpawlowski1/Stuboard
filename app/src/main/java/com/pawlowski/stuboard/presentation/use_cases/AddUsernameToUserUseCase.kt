package com.pawlowski.stuboard.presentation.use_cases

import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.data.authentication.AuthenticationResult

fun interface AddUsernameToUserUseCase: suspend (FirebaseUser, String) -> AuthenticationResult