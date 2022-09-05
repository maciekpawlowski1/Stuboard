package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.AuthResult


interface IAuthManager {
    suspend fun isSignedIn(): Boolean
    suspend fun signInWithPassword(mail: String, password: String): AuthResult?
    suspend fun registerWithPassword(mail: String, password: String): AuthResult?
    suspend fun signInWithGoogle()
    suspend fun getApiToken(): String?
}