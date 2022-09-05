package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountsRepository @Inject constructor(
    private val authManager: IAuthManager
): IAccountsRepository {
    override suspend fun getLogInState(): AppLoginState {
        return if(authManager.isSignedIn())
            AppLoginState.LoggedIn
        //TODO: Add if logged anonymously
        else {
            AppLoginState.NotLoggedIn
        }
    }

    override fun observeLogInState(): Flow<AppLoginState> = flow {
        TODO("Not implemented yet")
    }

}