package com.pawlowski.stuboard.presentation.use_cases

import com.google.firebase.auth.FirebaseUser

fun interface GetCurrentUserUseCase: () -> FirebaseUser?