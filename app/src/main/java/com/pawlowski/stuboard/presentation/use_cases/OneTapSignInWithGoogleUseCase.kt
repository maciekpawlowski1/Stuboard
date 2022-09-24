package com.pawlowski.stuboard.presentation.use_cases

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.pawlowski.stuboard.domain.models.Response
import kotlinx.coroutines.flow.Flow

fun interface OneTapSignInWithGoogleUseCase: () -> Flow<Response<BeginSignInResult>>
