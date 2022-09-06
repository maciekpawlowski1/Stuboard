package com.pawlowski.stuboard.domain

import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow

interface IAccountsRepository {
    fun getLogInState(): AppLoginState
    fun observeLogInState(): Flow<AppLoginState>
    suspend fun logInWithEmailAndPassword(email: String, password: String): AuthenticationResult
    suspend fun registerWithEmailAndPassword(email: String, password: String): AuthenticationResult
    suspend fun addUsernameToUser(user: FirebaseUser, username: String): AuthenticationResult

}