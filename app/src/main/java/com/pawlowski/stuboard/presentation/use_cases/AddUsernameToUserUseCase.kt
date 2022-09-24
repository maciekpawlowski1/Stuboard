package com.pawlowski.stuboard.presentation.use_cases

import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.domain.models.Resource

fun interface AddUsernameToUserUseCase: suspend (FirebaseUser, String) -> Resource<FirebaseUser>