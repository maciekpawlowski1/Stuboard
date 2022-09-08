package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.FirebaseUser

sealed class AuthenticationResult {
    data class Success(
        val user: FirebaseUser,
    ): AuthenticationResult()

    data class Failure(
        val errorMessage: String?,
    ): AuthenticationResult()

}