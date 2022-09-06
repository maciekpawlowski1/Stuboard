package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow

interface IAccountsRepository {
    suspend fun getLogInState(): AppLoginState
    fun observeLogInState(): Flow<AppLoginState>
    suspend fun logInWithEmailAndPassword(email: String, password: String): AuthenticationResult
    suspend fun registerWithEmailAndPassword(email: String, password: String): AuthenticationResult

}