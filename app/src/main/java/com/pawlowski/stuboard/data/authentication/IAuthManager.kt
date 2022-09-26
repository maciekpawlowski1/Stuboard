package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.domain.models.Resource
import kotlinx.coroutines.flow.Flow


interface IAuthManager {
    fun isSignedIn(): Boolean
    suspend fun signInWithPassword(mail: String, password: String): Resource<FirebaseUser>
    suspend fun registerWithPassword(mail: String, password: String): Resource<FirebaseUser>
    suspend fun signInWithCredentials(authCredential: AuthCredential): Result<AuthResult>
    suspend fun addUsernameToUser(user: FirebaseUser, username: String): Resource<FirebaseUser>
    suspend fun getApiToken(): String?
    fun getCurrentUser(): FirebaseUser?
    fun observeAuthState() : Flow<FirebaseAuth>
    fun signOut()
    suspend fun getUserRole(): String?
}