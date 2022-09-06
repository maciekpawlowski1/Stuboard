package com.pawlowski.stuboard.domain

import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRepository @Inject constructor(
    private val authManager: IAuthManager
): IAccountsRepository {


    override fun getLogInState(): AppLoginState {
        return if(authManager.isSignedIn())
            AppLoginState.LoggedIn
        //TODO: Add if logged anonymously
        else {
            AppLoginState.NotLoggedIn
        }
    }

    override fun observeLogInState(): Flow<AppLoginState> = flow {
        authManager.observeAuthState().collect {
            val user = it.currentUser
            if(user != null)
                emit(AppLoginState.LoggedIn)
            else if(false) //TODO: Add if logged anonymously
                emit(AppLoginState.LoggedInAnonymously)
            else
                emit(AppLoginState.NotLoggedIn)
        }
    }

    override suspend fun logInWithEmailAndPassword(email: String, password: String): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.signInWithPassword(email, password)
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.registerWithPassword(email, password)
        }
    }

    override suspend fun addUsernameToUser(
        user: FirebaseUser,
        username: String
    ): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.addUsernameToUser(user, username)
        }
    }

}