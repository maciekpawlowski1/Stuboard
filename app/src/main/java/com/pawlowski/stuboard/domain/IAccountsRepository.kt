package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow

interface IAccountsRepository {
    suspend fun getLogInState(): AppLoginState
    fun observeLogInState(): Flow<AppLoginState>
}