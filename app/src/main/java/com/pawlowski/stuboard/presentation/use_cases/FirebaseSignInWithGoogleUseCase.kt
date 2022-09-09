package com.pawlowski.stuboard.presentation.use_cases

import com.google.firebase.auth.AuthCredential
import com.pawlowski.stuboard.domain.models.Response
import kotlinx.coroutines.flow.Flow

fun interface FirebaseSignInWithGoogleUseCase: (AuthCredential) -> Flow<Response<Boolean>>