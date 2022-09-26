package com.pawlowski.stuboard.domain.auth

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.domain.models.Response
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow

interface IAccountsRepository {
    fun getLogInState(): AppLoginState
    fun observeLogInState(): Flow<AppLoginState>
    suspend fun logInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser>
    suspend fun registerWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser>
    suspend fun addUsernameToUser(user: FirebaseUser, username: String): Resource<FirebaseUser>
    fun oneTapSignInWithGoogle(): Flow<Response<BeginSignInResult>>
    fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<Response<Boolean>>
    fun getCurrentUser(): FirebaseUser?
    fun signOut()
    suspend fun isUserAdmin(): Boolean?
}