package com.pawlowski.stuboard.data.authentication



interface IAuthManager {
    suspend fun isSignedIn(): Boolean
    suspend fun signInWithPassword(mail: String, password: String): AuthenticationResult
    suspend fun registerWithPassword(mail: String, password: String): AuthenticationResult
    suspend fun signInWithGoogle()
    suspend fun getApiToken(): String?
}